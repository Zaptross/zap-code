from abc import ABC, abstractmethod
from typing import Callable
from bson.objectid import ObjectId
from database.job import Job
from database.job_status import JobStatus

class Task(ABC):
  @property
  @staticmethod
  def id() -> ObjectId:
    pass

  @abstractmethod
  def __init__(self, job: Job):
    pass

  @property
  @abstractmethod
  def timeout_seconds(self) -> int:
    """The maximum time the task is allowed to run before timing out."""
    pass

  @abstractmethod
  def run(self, on_complete: Callable[[Job, JobStatus], None]) -> JobStatus:
    """Run the task and call on_complete with the task id and the result."""
    pass

  def solutionReplacementTarget(self) -> str:
    return "# SOLUTION_INSERTION_TARGET #"
