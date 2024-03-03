from transport.transport import Transport

class CaptureTransport(Transport):
  def __init__(self):
    self.logs = []

  def write(self, message: str):
    self.logs.append(message)
  
  def get_logs(self):
    return self.logs