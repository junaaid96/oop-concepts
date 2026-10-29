import math
import unittest

import oop_vs_fp as m


class OopVsFpTest(unittest.TestCase):
    items = [m.Item("pen", 2.5, 4), m.Item("book", 10, 1)]

    def test_cart_same_result(self):
        cart = m.Cart()
        for i in self.items:
            cart.add(i)
        cart.apply_discount(10)
        self.assertEqual(cart.total(), m.cart_total(self.items, 10))
        self.assertEqual(m.cart_total(self.items), 20)

    def test_shapes(self):
        self.assertAlmostEqual(m.CircleOOP(1).area(), m.area(m.Circle(1)))
        self.assertEqual(m.perimeter(m.Square(2)), 8)

    def test_failure_handling(self):
        stock = {"pen": 3}
        self.assertEqual(m.place_order(stock, "pen", 2), m.Ok({"pen": 1}))
        self.assertEqual(stock, {"pen": 3})                 # input untouched
        self.assertEqual(m.place_order(stock, "pen", 0), m.Err("qty must be positive"))
        self.assertEqual(m.place_order(stock, "pen", 9), m.Err("out of stock"))
        proc = m.OrderProcessorOOP({"pen": 3})
        self.assertEqual(proc.place("pen", 2), 1)
        with self.assertRaises(m.OrderError):
            proc.place("pen", 9)

    def test_account(self):
        acc = m.Account()
        acc.deposit(100); acc.withdraw(30)
        events = [("deposit", 100), ("withdraw", 30)]
        self.assertEqual(acc.balance, m.balance_from_events(events))

    def test_strategy(self):
        self.assertEqual(m.checkout_oop(100, m.BlackFriday()), m.checkout_fp(100, lambda b: b * 0.5))

    def test_parallel(self):
        chunks = ["a b a", "b c", "a"]
        shared = m.SharedCounter()
        for c in chunks:
            shared.add_text(c)
        self.assertEqual(m.parallel_count(chunks), shared.counts)
        self.assertEqual(m.parallel_count(chunks)["a"], 3)

    def test_hybrid(self):
        s = m.Settings()
        self.assertEqual(s.with_theme("dark").theme, "dark")
        self.assertEqual(s.theme, "light")


if __name__ == "__main__":
    unittest.main()
