import unittest

from equality import Color, Customer, NoHash, aliasing_demo


class EqualityTest(unittest.TestCase):
    def test_entity(self):
        a, b = Customer(1, "Ana"), Customer(1, "Ana Maria")
        self.assertEqual(a, b)
        self.assertIsNot(a, b)
        self.assertEqual(len({a, b}), 1)

    def test_value_object(self):
        self.assertEqual(Color(1, 2, 3), Color(1, 2, 3))
        self.assertIn(Color(1, 2, 3), {Color(1, 2, 3)})

    def test_unhashable(self):
        with self.assertRaises(TypeError):
            hash(NoHash())

    def test_copies(self):
        original, shallow, deep = aliasing_demo()
        self.assertEqual(original[1], [2, 3, 4])
        self.assertEqual(shallow[1], [2, 3, 4])
        self.assertEqual(deep[1], [2, 3])


if __name__ == "__main__":
    unittest.main()
