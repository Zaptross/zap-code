from abc import ABC, abstractmethod
from typing import Callable
from database.job import Job

class Task(ABC):
  @property
  @staticmethod
  def id() -> str:
    pass

  @abstractmethod
  def __init__(self, job: Job):
    pass

  @property
  @abstractmethod
  def timeout_seconds(self) -> int:
    pass

  @abstractmethod
  def run(self, on_complete: Callable[[str, str], None]) -> None:
    """Run the task and call on_complete with the task id and the result."""
    pass

  def solutionReplacementTarget(self) -> str:
    return "# SOLUTION_INSERTION_TARGET #"
