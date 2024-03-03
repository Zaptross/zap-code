from transport.transport import Transport

class CaptureTransport(Transport):
  logs = []

  def write(self, message: str):
    self.logs.append(message)
  
  def get_logs(self):
    return self.logs