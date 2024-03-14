class User:
  def __init__(self) -> None:
    self.id = ""

  def fromDict(d: dict):
      usr = User()
      if d is None:
          return None
      if "_id" in d:
        usr.id = d["_id"]

      return usr