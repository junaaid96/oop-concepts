import unittest

from anti_patterns import (AnemicOrder, Creature, EmailValidator, Mailer, OrderService,
                           Registration, RichOrder, UserStore)


class AntiPatternsTest(unittest.TestCase):
    def test_anemic_allows_invalid_state(self):
        o = AnemicOrder()
        OrderService.pay(o)
        self.assertEqual(o.status, "paid")      # paid with no items: bug allowed

    def test_rich_model_enforces_rules(self):
        o = RichOrder()
        with self.assertRaises(ValueError):
            o.pay()
        o.add(10); o.pay()
        self.assertEqual(o.status, "paid")
        with self.assertRaises(ValueError):
            o.add(5)

    def test_composition_and_split(self):
        self.assertEqual(Creature("robodog", "beep-woof").speak(), "robodog: beep-woof")
        reg = Registration(EmailValidator(), UserStore(), m := Mailer())
        reg.register("a@b.c")
        self.assertEqual(m.outbox, ["welcome a@b.c"])


if __name__ == "__main__":
    unittest.main()
