class User:
    id = ""
    email = ""
    auth_provider = ""

    def fromDict(d: dict):
        usr = User()
        if d is None:
            return None
        if "_id" in d:
          usr.id = d["_id"]
        if "email" in d:
          usr.email = d["email"]
        if "authProvider" in d:
          usr.auth_provider = d["authProvider"]

        return usr