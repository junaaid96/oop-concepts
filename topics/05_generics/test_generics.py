import unittest

from generics import Box, Repository, first, smallest


class GenericsTest(unittest.TestCase):
    def test_box_map(self):
        self.assertEqual(Box(21).map(lambda x: x * 2).map(str), Box("42"))

    def test_functions(self):
        self.assertEqual(first(["a", "b"]), "a")
        self.assertIsNone(first([]))
        self.assertEqual(smallest([3, 1, 2]), 1)
        self.assertEqual(smallest(["b", "a"]), "a")

    def test_repository(self):
        repo: Repository[str] = Repository()
        repo.add(1, "Ana")
        self.assertEqual(repo.get(1), "Ana")
        self.assertIsNone(repo.get(2))


if __name__ == "__main__":
    unittest.main()
