from transport.transport import ConsoleTransport
from runner.tester import Tester

def withTestingContext(impl: str, tests: str):
  with open("./runner/tester.py", "r") as f:
    return "%s\n\n%s\n\n%s" % (f.read(), impl, tests)

tp = ConsoleTransport()
tp.write("Testing string formatting %s %d" % ("with a string", 7))

t = Tester(tp)

with open("test.txt", "r") as f:

  fc = f.read()
  tp.write("Testing file contents: \n\n%s" % fc)

  try:
    exec(
      withTestingContext(
        fc,
        """
t.assert_not_throws("sum() - does not throw", lambda: sum(1, 2))
t.assert_eq("sum() - adds correctly", sum(1, 2), 3)
"""
      )
    )
  except Exception as e:
    tp.write("FAIL\n%s" % e)