from pymongo.collection import Collection

from .logs import Logs

def write_logs(logs: Collection, l: Logs):
  logs.insert_one(l.to_dict())