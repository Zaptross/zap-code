from abc import ABC, abstractmethod
from database.job import Job

class JobProvider(ABC):
  @abstractmethod
  def next(self) -> Job:
    """Returns the next job to be executed. If there are no jobs, returns None."""
    pass

  @abstractmethod
  def on_complete(self, job: Job):
    """Called to notify the provider that a job has been completed."""
    pass