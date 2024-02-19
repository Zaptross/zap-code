from transport.transport import Transport

class Tester:
    def __init__(self, transport: Transport):
        self.transport = transport

    def do_assert(self, name: str, result: bool, input: str, expected: str):
        if result:
            self.transport.write("PASS - %s" % name)
        else:
            self.transport.write("FAIL - %s\nExpected: \n\t%s\nReceived: \n\t%s" % (name, expected, input))

    # Equality comparisons

    def assert_eq(self, name: str, input, expected):
        self.do_assert(name, input == expected, input, expected)
    
    def assert_ne(self, name: str, input, expected):
        self.do_assert(name, input != expected, input, expected)

    # Numeric comparisons

    def assert_lt(self, name: str, input, expected):
        self.do_assert(name, input < expected, input, expected)
    
    def assert_gt(self, name: str, input, expected):
        self.do_assert(name, input > expected, input, expected)
    
    def assert_lte(self, name: str, input, expected):
        self.do_assert(name, input <= expected, input, expected)
    
    def assert_gte(self, name: str, input, expected):
        self.do_assert(name, input >= expected, input, expected)

    # Boolean comparisons

    def assert_true(self, name: str, input: bool):
        self.do_assert(name, input, input, "True")
    
    def assert_false(self, name: str, input: bool):
        self.do_assert(name, not input, input, "False")
    
    # Other comparisons

    def assert_throws(self, name: str, func, exception):
        try:
            func()
            self.transport.write("FAIL\n%s did not throw %s" % (name, exception))
        except exception:
            self.transport.write("PASS - %s" % name)
        except:
            self.transport.write("FAIL\n%s threw wrong exception" % name)
        else:
            self.transport.write("FAIL\n%s did not throw" % name)
    
    def assert_throws_any(self, name: str, func):
        try:
            func()
            self.transport.write("FAIL\n%s did not throw" % name)
        except:
            self.transport.write("PASS - %s" % name)
    
    def assert_not_throws(self, name: str, func):
        try:
            func()
            self.transport.write("PASS - %s" % name)
        except:
            self.transport.write("FAIL\n%s threw" % name)
    
    def assert_not_throws_any(self, name: str, func):
        try:
            func()
            self.transport.write("PASS - %s" % name)
        except:
            self.transport.write("FAIL\n%s threw" % name)