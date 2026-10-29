import unittest

from solid import (FakeSender, FlatDiscount, InvoiceCalculator, InvoiceFormatter, InvoiceService,
                   LineItem, Penguin, Sparrow)

ITEMS = [LineItem("pen", 2.5, 4), LineItem("book", 10, 1)]


class SolidTest(unittest.TestCase):
    def test_calculator_with_extension(self):
        self.assertEqual(InvoiceCalculator().total(ITEMS), 20)
        self.assertEqual(InvoiceCalculator([FlatDiscount(5)]).total(ITEMS), 15)

    def test_lsp(self):
        self.assertEqual([b.move() for b in (Sparrow(), Penguin())], ["flies", "swims"])

    def test_dip_with_fake(self):
        sender = FakeSender()
        svc = InvoiceService(InvoiceCalculator(), InvoiceFormatter(), sender)
        self.assertEqual(svc.bill("ana@example.com", ITEMS), 20)
        self.assertIn("TOTAL 20.00", sender.sent[0][1])


if __name__ == "__main__":
    unittest.main()
