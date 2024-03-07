from .task import Task
from .fizzbuzz import FizzBuzz
from .reverseastring import ReverseAString

__all__ = [
  FizzBuzz,
  ReverseAString,
]

def get_task(id: str) -> Task:
  for task in __all__:
    if task.id == id:
      return task
  return None