from transport.transport import Transport

class CaptureTransport(Transport):
  def __init__(self):
    self.logs = []

  def write(self, message: str):
    if isinstance(message, str):
      self.logs.append(message)
    else:
      self.logs.append(str(message))
  
  def get_logs(self):
    return self.logs