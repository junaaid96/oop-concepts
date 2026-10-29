import unittest

from nested import Button, LinkedList, anonymous_greeter, make_validator


class NestedTest(unittest.TestCase):
    def test_nested(self):
        ll = LinkedList()
        for v in (3, 2, 1):
            ll.push_front(v)
        self.assertEqual(list(ll), [1, 2, 3])

    def test_inner(self):
        b = Button()
        b.clicker().click(); b.clicker().click()
        self.assertEqual(b.clicks, 2)

    def test_local_and_anonymous(self):
        at_least_3 = make_validator(3)
        self.assertTrue(at_least_3("abc"))
        self.assertFalse(at_least_3("ab"))
        self.assertEqual(anonymous_greeter().greet("Ana"), "hi Ana")


if __name__ == "__main__":
    unittest.main()
