import unittest
from datetime import datetime

from di import FixedClock, GreetingService, InMemoryUserRepo, composition_root


class DiTest(unittest.TestCase):
    def test_injected_fakes(self):
        svc = GreetingService(InMemoryUserRepo({1: "Ana"}), FixedClock(datetime(2026, 1, 1, 9)))
        logged = []
        svc.log = logged.append                       # setter injection
        self.assertEqual(svc.greet(1), "Good morning, Ana")
        self.assertEqual(svc.greet(2, str.upper), "GOOD MORNING, GUEST")
        self.assertEqual(len(logged), 2)

    def test_container_singleton(self):
        svc = composition_root()
        self.assertIn("Ana", svc.greet(1))


if __name__ == "__main__":
    unittest.main()
