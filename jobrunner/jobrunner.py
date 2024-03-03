from dotenv import load_dotenv
from pymongo import MongoClient

from database.config import MongoConfig
from database.user import User
from database.job import Job
from jobs.get_tasks import get_task
from database.job_status import JobStatus

load_dotenv()

mongoConfig = MongoConfig().fromEnvGroup("database")

client = MongoClient(mongoConfig.toConnectionString())
db = client.get_database("zapcode")
col = db.get_collection("users")
user = col.find_one({ "email": "test@testing.testing"})
print(user)
user = User.fromDict(user)
print(user, user.id, user.email, user.auth_provider)

jobs = db.get_collection("jobs")
job = Job.fromDict(jobs.find_one({ "status": JobStatus.PENDING.name }))

if job is None:
  print("Job not found")
  exit(1)

task = get_task(job.task_id)
fb = task(job)

if fb is None:
  print("Task not found")
  exit(1)

fb.run(lambda id, status: print(id, status))
for log in fb.transport.get_logs():
  print(log)