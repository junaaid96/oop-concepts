import dataclasses
import unittest

from classes import Money, Temperature, User


class ClassesTest(unittest.TestCase):
    def test_constructor_and_static(self):
        before = User.count
        u = User.from_csv("Bo, bo@example.com")
        self.assertEqual((u.name, u.email), ("Bo", "bo@example.com"))
        self.assertEqual(User.count, before + 1)
        with self.assertRaises(ValueError):
            User("x", "no-at-sign")

    def test_immutability(self):
        m = Money(10)
        with self.assertRaises(dataclasses.FrozenInstanceError):
            m.amount = 99
        self.assertEqual(m.add(Money(5)), Money(15))
        self.assertEqual(m.with_currency("EUR").currency, "EUR")
        self.assertEqual(m.currency, "USD")      # original unchanged
        self.assertEqual(len({Money(1), Money(1)}), 1)  # hashable value objects

    def test_property(self):
        self.assertEqual(Temperature(100).fahrenheit, 212)


if __name__ == "__main__":
    unittest.main()
