import unittest

from composition import BadStack, Car, ElectricEngine, PetrolEngine, ReadOnlyView, Stack


class CompositionTest(unittest.TestCase):
    def test_swap_part_at_runtime(self):
        car = Car(PetrolEngine())
        self.assertEqual(car.start(), "car: vroom")
        car.engine = ElectricEngine()
        self.assertEqual(car.start(), "car: hum")

    def test_stack_contract(self):
        self.assertTrue(hasattr(BadStack(), "insert"))   # leaky inheritance
        s = Stack()
        s.push(1); s.push(2)
        self.assertFalse(hasattr(s, "insert"))           # composition hides it
        self.assertEqual(s.pop(), 2)

    def test_delegation(self):
        view = ReadOnlyView([3, 1, 2])
        self.assertEqual(view.index(1), 1)
        self.assertEqual(len(view), 3)
        with self.assertRaises(AttributeError):
            view.append(4)


if __name__ == "__main__":
    unittest.main()
