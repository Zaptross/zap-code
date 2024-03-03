from transport.transport import Transport

class Tester:
    def __init__(self, transport: Transport):
        self.transport = transport
        self.tests = 0
        self.passes = 0
    
    def get_transport(self):
        return self.transport

    def do_assert(self, name: str, result: bool, input: str, expected: str, log = True):
        self.tests += 1
        if result:
            self.passes += 1
            if log:
                self.transport.write("PASS - %s" % name)
        else:
            if log:
                self.transport.write("FAIL - %s\nExpected: \n\t%s\nReceived: \n\t%s" % (name, expected, input))

    ## Logged assertions
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

    # List comparisons
    def assert_contains(self, name: str, input, expected):
        self.do_assert(name, expected in input, input, expected)
    
    def assert_not_contains(self, name: str, input, expected):
        self.do_assert(name, expected not in input, input, expected)
    
    def assert_len(self, name: str, input, expected):
        self.do_assert(name, len(input) == expected, len(input), expected)

    # Other comparisons

    def assert_throws(self, name: str, func, exception):
        self.tests += 1
        try:
            func()
            self.transport.write("FAIL\n%s did not throw %s" % (name, exception))
        except exception:
            self.transport.write("PASS - %s" % name)
            self.passes += 1
        except:
            self.transport.write("FAIL\n%s threw wrong exception" % name)
        else:
            self.transport.write("FAIL\n%s did not throw" % name)
    
    def assert_throws_any(self, name: str, func):
        self.tests += 1
        try:
            func()
            self.transport.write("FAIL\n%s did not throw" % name)
        except:
            self.transport.write("PASS - %s" % name)
            self.passes += 1
    
    def assert_not_throws(self, name: str, func):
        self.tests += 1
        try:
            func()
            self.transport.write("PASS - %s" % name)
            self.passes += 1
        except:
            self.transport.write("FAIL\n%s threw" % name)
    
    def assert_not_throws_any(self, name: str, func):
        self.tests += 1
        try:
            func()
            self.transport.write("PASS - %s" % name)
            self.passes += 1
        except:
            self.transport.write("FAIL\n%s threw" % name)

    ## Silent assertions
    # Equality comparisons

    def s_assert_eq(self, input, expected):
        self.do_assert("", input == expected, input, expected, False)
    
    def s_assert_ne(self, input, expected):
        self.do_assert("", input != expected, input, expected, False)

    # Numeric comparisons

    def s_assert_lt(self, input, expected):
        self.do_assert("", input < expected, input, expected, False)
    
    def s_assert_gt(self, input, expected):
        self.do_assert("", input > expected, input, expected, False)
    
    def s_assert_lte(self, input, expected):
        self.do_assert("", input <= expected, input, expected, False)
    
    def s_assert_gte(self, input, expected):
        self.do_assert("", input >= expected, input, expected, False)

    # Boolean comparisons

    def s_assert_true(self, input: bool):
        self.do_assert("", input, input, "True", False)
    
    def s_assert_false(self, input: bool):
        self.do_assert("", not input, input, "False", False)

    # List comparisons
    def s_assert_contains(self, input, expected):
        self.do_assert("", expected in input, input, expected, False)
    
    def s_assert_not_contains(self, input, expected):
        self.do_assert("", expected not in input, input, expected, False)
    
    def s_assert_len(self, input, expected):
        self.do_assert("", len(input) == expected, len(input), expected, False)

    # Other comparisons

    def s_assert_throws(self, func, exception):
        self.tests += 1
        try:
            func()
        except exception:
            self.passes += 1
        except:
            pass
        else:
            pass
    
    def s_assert_throws_any(self, func):
        self.tests += 1
        try:
            func()
        except:
            self.passes += 1
    
    def s_assert_not_throws(self, func):
        self.tests += 1
        try:
            func()
            self.passes += 1
        except:
            pass
    
    def s_assert_not_throws_any(self, func):
        self.tests += 1
        try:
            func()
            self.passes += 1
        except:
            pass