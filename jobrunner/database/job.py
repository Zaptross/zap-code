from database.job_status import JobStatus

class Job:
  def __init__(self):
    self.id = None
    self.task_id = None
    self.user_id = None
    self.status = JobStatus.PENDING
    self.solution = None

  def fromDict(d: dict):
    job = Job()
    if d is None:
      return None
    if "_id" in d:
      job.id = d["_id"]
    if "taskId" in d:
      job.task_id = d["taskId"]
    if "userId" in d:
      job.user_id = d["userId"]
    if "status" in d:
      job.status = JobStatus[d["status"]]
    if "solution" in d:
      job.solution = d["solution"]
  
    return job

  def to_dict(self):
    return {
      "_id": self.id,
      "taskId": self.task_id,
      "userId": self.user_id,
      "status": self.status.name,
      "solution": self.solution
    }
