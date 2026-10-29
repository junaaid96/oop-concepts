import unittest

from contracts import Bike, Car, Exporter, JsonExporter, Vehicle, save_report


class ContractsTest(unittest.TestCase):
    def test_protocol(self):
        self.assertIsInstance(JsonExporter(), Exporter)
        self.assertEqual(save_report(JsonExporter(), [[1, "a"]]), '[[1, "a"]]')

    def test_abstract_class(self):
        with self.assertRaises(TypeError):
            Vehicle("x")                      # cannot instantiate abstract class
        self.assertEqual(Car("VW").summary(), "VW with 4 wheels")
        self.assertEqual(Bike("BMX").wheels(), 2)


if __name__ == "__main__":
    unittest.main()
