from bson.objectid import ObjectId
from importlib import resources
from . import templates

from database.logs import Logs
from database.job_status import JobStatus
from jobs.task import Task
from runner.tester import Tester
from transport.capture import CaptureTransport

class ReverseAString(Task):
  id = ObjectId('65e8f6ad651cebb033a7688a')

  def __init__(self, job):
    self.executable = resources.read_text(templates, "reverseastring.py")
    self.runTransport = CaptureTransport()
    self.testTransport = CaptureTransport()
    self.job = job

  @property
  def timeout_seconds(self):
    return 5


  def run(self, on_complete):
    result = JobStatus.PENDING
    toExec = self.executable.replace(self.solutionReplacementTarget(), self.job.solution)

    customGlobals = dict()
    customGlobals["print"] = lambda *args: self.runTransport.write(*args)

    try:
      result = JobStatus.RUNNING
      exec(toExec, customGlobals)
      did_pass = self.test_reverse_a_string()

      if did_pass:
        result = JobStatus.PASS
      else:
        result = JobStatus.FAILED
      
    except Exception as e:
      self.runTransport.write("Error: %s" % e)
      result = JobStatus.FAILED
    finally:
      on_complete(self.job, result)
      return result
  
  def get_logs(self):
    l = Logs()
    l.id = self.job.id
    l.user_id = self.job.user_id
    l.task_id = self.id
    l.logs = list(self.runTransport.get_logs())
    l.tests = list(self.testTransport.get_logs())

    return l

  def test_reverse_a_string(self):
    logs = list(self.runTransport.get_logs())
    t = Tester(self.testTransport)

    t.assert_contains(
      "Hello World -> dlroW olleH",
      logs,
      "dlroW olleH"
    )
    t.assert_contains(
      "zyxwvutsrqponmlkjihgfedcba -> abcdefghijklmnopqrstuvwxyz",
      logs,
      "zyxwvutsrqponmlkjihgfedcba"
    )
    t.assert_contains(
      "lorem ipsum dolor sit amet",
      logs,
      "tema tis rolod muspi merol"
    )

    t.report_suite()

    return t.suite_passed()
