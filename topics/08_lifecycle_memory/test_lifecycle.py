import unittest

from lifecycle import Connection, cycle_is_collected, transaction


class LifecycleTest(unittest.TestCase):
    def test_context_manager_closes_on_error(self):
        start = Connection.open_count
        with self.assertRaises(RuntimeError):
            with Connection("db") as conn:
                raise RuntimeError("boom")
        self.assertTrue(conn.closed)
        self.assertEqual(Connection.open_count, start)

    def test_transaction(self):
        log = []
        with transaction(log):
            pass
        with self.assertRaises(ValueError):
            with transaction(log):
                raise ValueError
        self.assertEqual(log, ["begin", "commit", "begin", "rollback"])

    def test_gc(self):
        self.assertTrue(cycle_is_collected())


if __name__ == "__main__":
    unittest.main()
