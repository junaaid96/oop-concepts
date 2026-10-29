import unittest

from errors import AppError, GatewayError, InsufficientFunds, PaymentError, Wallet, charge_card, transfer


class ErrorsTest(unittest.TestCase):
    def test_hierarchy(self):
        with self.assertRaises(PaymentError) as ctx:      # catch by base class
            Wallet("a", 5).pay(10)
        self.assertIsInstance(ctx.exception, InsufficientFunds)
        self.assertIsInstance(ctx.exception, AppError)
        self.assertEqual(ctx.exception.needed, 10)

    def test_strong_guarantee(self):
        a, b = Wallet("a", 5), Wallet("b", 0)
        with self.assertRaises(InsufficientFunds):
            transfer(a, b, 10)
        self.assertEqual((a.balance, b.balance), (5, 0))
        transfer(a, b, 5)
        self.assertEqual((a.balance, b.balance), (0, 5))

    def test_chaining(self):
        def down():
            raise ConnectionError("timeout")
        with self.assertRaises(GatewayError) as ctx:
            charge_card(down)
        self.assertIsInstance(ctx.exception.__cause__, ConnectionError)


if __name__ == "__main__":
    unittest.main()
