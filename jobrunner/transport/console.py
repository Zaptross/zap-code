from transport.transport import Transport

class ConsoleTransport(Transport):

  def write(self, message: str):
    print(message)
