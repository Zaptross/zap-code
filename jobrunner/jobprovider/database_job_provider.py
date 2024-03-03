from pymongo.database import Database

from .job_provider import JobProvider
from database.job import Job
from database.job_status import JobStatus

class DatabaseJobProvider(JobProvider):
  def __init__(self, db: Database):
    self.jobs = db.get_collection("jobs")
  
  def next(self) -> Job:
    return Job.fromDict(self.jobs.find_one({ "status": "PENDING" }))
  
  def on_complete(self, job: Job, new_status: JobStatus):
    self.jobs.update_one({ "_id": job.id }, { "$set": { "status": new_status.name } })