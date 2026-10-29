import unittest

from patterns import (AddText, Document, EventBus, Free, LegacyPrinter, LegacyPrinterAdapter,
                      LoggingNotifier, Order, PizzaBuilder, Standard, make_notifier)


class PatternsTest(unittest.TestCase):
    def test_strategy(self):
        o = Order(2, Standard())
        self.assertEqual(o.shipping_cost(), 7)
        o.shipping = Free()
        self.assertEqual(o.shipping_cost(), 0)

    def test_factory_decorator_adapter(self):
        log = []
        n = LoggingNotifier(make_notifier("sms"), log)
        self.assertEqual(n.send("hi"), "sms:hi")
        self.assertEqual(log, ["sending hi"])
        self.assertEqual(LegacyPrinterAdapter(LegacyPrinter()).send("hi"), "HI")

    def test_builder(self):
        p = PizzaBuilder().size("large").topping("olive").topping("cheese").build()
        self.assertEqual((p.size, p.toppings), ("large", ("olive", "cheese")))

    def test_observer(self):
        bus, got = EventBus(), []
        bus.subscribe("order", got.append)
        bus.publish("order", 42)
        self.assertEqual(got, [42])

    def test_command_undo(self):
        d = Document()
        d.run(AddText("Hello")); d.run(AddText(" world"))
        d.undo()
        self.assertEqual(d.text, "Hello")


if __name__ == "__main__":
    unittest.main()
