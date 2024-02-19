from abc import ABC, abstractmethod

class Transport(ABC):

  @abstractmethod
  def write(self, message: str):
    pass

class ConsoleTransport(Transport):

  def write(self, message: str):
    print(message)