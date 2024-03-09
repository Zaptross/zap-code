from bson.objectid import ObjectId

class Logs:
  def __init__(self):
    self.id: ObjectId
    self.user_id: ObjectId
    self.task_id: ObjectId
    self.tests: list[str] = []
    self.logs: list[str] = []

  def from_dict(data: dict):
    l = Logs()
    if "_id" in data:
      l.id = data["_id"]
    if "jobId" in data:
      l.user_id = data["user_id"]
    if "taskId" in data:
      l.task_id = data["task_id"]
    if "tests" in data:
      l.tests = data["tests"]
    if "logs" in data:
      l.logs = data["logs"]

    return l

  def to_dict(self) -> dict:
    return {
      "_id": self.id,
      "userId": self.user_id,
      "taskId": self.task_id,
      "tests": self.tests,
      "logs": self.logs
    }