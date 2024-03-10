from os import getenv

class MongoConfig:
  def __init__(self):
    self.user = ""
    self.password = ""
    self.database = ""
    self.host = ""
    self.port = ""

  def toConnectionString(self):
    return f"mongodb://{self.user}:{self.password}@{self.host}:{self.port}"
  
  def fromEnvGroup(self, group: str):
    hostKey = f"{group}_HOST".upper()
    self.host = getenv(hostKey) or getenv(hostKey.lower())
    portKey = f"{group}_PORT".upper()
    self.port = getenv(portKey) or getenv(portKey.lower())
    userKey = f"{group}_USER".upper()
    self.user = getenv(userKey) or getenv(userKey.lower())
    passwordKey = f"{group}_PASSWORD".upper()
    self.password = getenv(passwordKey) or getenv(passwordKey.lower())
    databaseKey = f"{group}_DATABASE".upper()
    self.database = getenv(databaseKey) or getenv(databaseKey.lower())

    return self