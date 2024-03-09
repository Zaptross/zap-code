from bson.objectid import ObjectId
from importlib import resources
from . import templates

from database.logs import Logs
from database.job_status import JobStatus
from jobs.task import Task
from runner.tester import Tester
from transport.capture import CaptureTransport

class FizzBuzz(Task):
  id = ObjectId('65e456500575b443b71a8846')

  def __init__(self, job):
    self.executable = resources.read_text(templates, "fizzbuzz.py")
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
      did_pass = self.test_fizz_buzz()

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
    l.task_id = FizzBuzz.id
    l.logs = list(self.runTransport.get_logs())
    l.tests = list(self.testTransport.get_logs())

    return l

  def test_fizz_buzz(self):
    logs = self.runTransport.get_logs()
    t = Tester(self.testTransport)

    t.assert_eq("1 = 1", logs[0], "1")
    t.assert_eq("2 = 2", logs[1], "2")
    t.assert_eq("3 = Buzz", logs[2], "Fizz")
    t.assert_eq("4 = 4", logs[3], "4")
    t.assert_eq("5 = Fizz", logs[4], "Buzz")
    t.assert_eq("15 = FizzBuzz", logs[14], "FizzBuzz")
    t.assert_len("length = 100", logs, 100)

    # silent tests
    t.s_assert_eq(logs[29], "FizzBuzz")
    t.s_assert_eq(logs[59], "FizzBuzz")
    t.s_assert_eq(logs[89], "FizzBuzz")
    t.s_assert_eq(logs[98], "Fizz")
    t.s_assert_eq(logs[99], "Buzz")

    t.report_suite()

    return t.suite_passed()
