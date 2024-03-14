from dotenv import load_dotenv
from pymongo import MongoClient
from time import sleep
from datetime import datetime

from database.config import MongoConfig
from database.job_status import JobStatus
from database.write_logs import write_logs
from jobs.task import Task
from jobs.get_tasks import get_task
from jobprovider.database_job_provider import DatabaseJobProvider
from transport.console import ConsoleTransport

load_dotenv()

mongoConfig = MongoConfig().fromEnvGroup("database")

client = MongoClient(mongoConfig.toConnectionString())
db = client.get_database(mongoConfig.database)

jp = DatabaseJobProvider(db)
cl = ConsoleTransport()
logs = db.get_collection("logs")

waitingForJobs = False
while True:
  job = jp.next()

  if job is None:
    if not waitingForJobs:
      cl.write("No jobs found, waiting...")
      waitingForJobs = datetime.now()
    sleep(1)
    continue

  if waitingForJobs is not False:
    waited = datetime.now() - waitingForJobs
    cl.write(f"Waited {waited.seconds} seconds for a job")
  waitingForJobs = False

  taskClass = get_task(job.task_id)
  if taskClass is None:
    cl.write("Task not found")
    jp.on_complete(job, JobStatus.FAILED)
    continue

  task: Task = taskClass(job)

  if task is None:
    cl.write("Task not found")
    jp.on_complete(job, JobStatus.FAILED)
    continue

  status = task.run(jp.on_complete)
  write_logs(logs, task.get_logs())
  cl.write("Job %s completed as %s" % (job.id, status.name))
