import math
import unittest

from pillars import (BankAccount, Circle, Duck, Rectangle, Robot, Stack, Vector2,
                     chorus, to_text, total_area)


class PillarsTest(unittest.TestCase):
    def test_encapsulation_guards_invariants(self):
        acc = BankAccount("Ana")
        acc.deposit(50)
        with self.assertRaises(ValueError):
            acc.withdraw(100)
        with self.assertRaises(AttributeError):
            acc.balance = 1_000_000  # no setter: read-only property
        self.assertEqual(acc.balance, 50)

    def test_dynamic_polymorphism(self):
        self.assertAlmostEqual(total_area([Circle(1), Rectangle(2, 3)]), math.pi + 6)
        self.assertIn("Circle", Circle(1).describe())

    def test_ad_hoc_polymorphism(self):
        self.assertEqual(Vector2(1, 2) + Vector2(3, 4), Vector2(4, 6))
        self.assertEqual(to_text([1.5, "x"]), "1.50, x")

    def test_parametric_and_duck_typing(self):
        s: Stack[str] = Stack()
        s.push("a")
        self.assertEqual(s.pop(), "a")
        self.assertEqual(chorus([Duck(), Robot()]), "Quack Beep")


if __name__ == "__main__":
    unittest.main()
