from .task import Task
from .fizzbuzz import FizzBuzz

__all__ = [
  FizzBuzz,
]

def get_task(id: str) -> Task:
  for task in __all__:
    if task.id == id:
      return task
  return None