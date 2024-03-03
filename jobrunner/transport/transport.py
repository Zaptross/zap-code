from abc import ABC, abstractmethod

class Transport(ABC):

  @abstractmethod
  def write(self, message: str):
    pass
