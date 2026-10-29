import unittest

from contracts_dbc import Account, ContractError


class DbcTest(unittest.TestCase):
    def test_contracts(self):
        acc = Account()
        self.assertEqual(acc.deposit(10), 10)
        with self.assertRaises(ContractError):
            acc.deposit(0)               # precondition
        with self.assertRaises(ContractError):
            acc.unsafe_set(-1)           # invariant


if __name__ == "__main__":
    unittest.main()
