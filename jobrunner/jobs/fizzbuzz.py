from importlib import resources
from . import templates

from database.job_status import JobStatus
from jobs.task import Task
from runner.tester import Tester
from transport.capture import CaptureTransport

class FizzBuzz(Task):
  id = "2b774897-bc1c-4714-9165-ca9cb8a2995f"

  def __init__(self, job):
    self.executable = resources.read_text(templates, "fizzbuzz.py")
    self.transport = CaptureTransport()
    self.job = job

  @property
  def timeout_seconds(self):
    return 5


  def run(self, on_complete):
    result = JobStatus.PENDING
    toExec = self.executable.replace(self.solutionReplacementTarget(), self.job.solution)

    customGlobals = dict()
    customGlobals["print"] = lambda *args: self.transport.write(*args)

    try:
      result = JobStatus.RUNNING
      exec(toExec, customGlobals)
      tests, passes = self.test_fizz_buzz()

      if tests == passes:
        result = JobStatus.PASS
      else:
        result = JobStatus.FAILED
      
      self.transport.write("Tests: %d/%d" % (passes, tests))
    except Exception as e:
      self.transport.write("Error: %s" % e)
      result = JobStatus.FAILED
    finally:
      on_complete(FizzBuzz.id, result)

  def test_fizz_buzz(self):
    logs = list(self.transport.get_logs())
    t = Tester(CaptureTransport())
    t.assert_eq("1 = 1", logs[0], 1)
    t.assert_eq("2 = 2", logs[1], 2)
    t.assert_eq("3 = Buzz", logs[2], "Fizz")
    t.assert_eq("4 = 4", logs[3], 4)
    t.assert_eq("5 = Fizz", logs[4], "Buzz")
    t.assert_eq("15 = FizzBuzz", logs[14], "FizzBuzz")
    t.assert_len("length = 100", logs, 100)

    # silent tests
    t.s_assert_eq(logs[29], "FizzBuzz")
    t.s_assert_eq(logs[59], "FizzBuzz")
    t.s_assert_eq(logs[89], "FizzBuzz")
    t.s_assert_eq(logs[98], "Fizz")
    t.s_assert_eq(logs[99], "Buzz")

    return t.tests, t.passes
