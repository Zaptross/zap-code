from dotenv import load_dotenv
from pymongo import MongoClient
from time import sleep

from database.config import MongoConfig
from database.user import User
from database.job import Job
from jobs.get_tasks import get_task
from database.job_status import JobStatus
from jobprovider.database_job_provider import DatabaseJobProvider
from transport.console import ConsoleTransport
from jobs.task import Task

load_dotenv()

mongoConfig = MongoConfig().fromEnvGroup("database")

client = MongoClient(mongoConfig.toConnectionString())
db = client.get_database("zapcode")

jp = DatabaseJobProvider(db)
cl = ConsoleTransport()

while True:
  job = jp.next()

  if job is None:
    cl.write("No jobs found, waiting for 5 seconds...")
    sleep(5)
    continue

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
  cl.write("Job %s completed as %s" % (job.id, status.name))
