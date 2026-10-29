import math
import unittest

from modeling import Circle, OrderStatus, Point, Square, area


class ModelingTest(unittest.TestCase):
    def test_enum(self):
        self.assertEqual(OrderStatus("paid"), OrderStatus.PAID)
        self.assertTrue(OrderStatus.SHIPPED.is_final())
        self.assertFalse(OrderStatus.PENDING.is_final())

    def test_record(self):
        self.assertEqual(Point(1, 2), Point(1, 2))
        self.assertEqual(len({Point(1, 2), Point(1, 2)}), 1)

    def test_sum_type(self):
        self.assertAlmostEqual(area(Circle(1)), math.pi)
        self.assertEqual(area(Square(3)), 9)
        with self.assertRaises(AssertionError):
            area("not a shape")


if __name__ == "__main__":
    unittest.main()
