"""Identity vs equality, value objects vs entities, and the hash contract."""
from __future__ import annotations

import copy


class Customer:
    """Entity: two objects with the same id are the same customer even if names differ."""

    def __init__(self, id_: int, name: str) -> None:
        self.id = id_
        self.name = name

    def __eq__(self, other: object) -> bool:
        return isinstance(other, Customer) and self.id == other.id

    def __hash__(self) -> int:          # consistent with __eq__: uses only the id
        return hash(self.id)


class Color:
    """Value object: equal when all fields are equal."""

    __slots__ = ("r", "g", "b")

    def __init__(self, r: int, g: int, b: int) -> None:
        self.r, self.g, self.b = r, g, b

    def _key(self) -> tuple[int, int, int]:
        return (self.r, self.g, self.b)

    def __eq__(self, other: object) -> bool:
        return isinstance(other, Color) and self._key() == other._key()

    def __hash__(self) -> int:
        return hash(self._key())


class NoHash:
    """Defining __eq__ without __hash__ makes the class unhashable."""

    def __eq__(self, other: object) -> bool:
        return True


def aliasing_demo() -> tuple[list[int], list[int], list[int]]:
    original = [1, [2, 3]]
    alias = original                    # same object (reference semantics)
    shallow = copy.copy(original)       # new outer list, shared inner list
    deep = copy.deepcopy(original)      # fully independent copy
    alias[1].append(4)                  # visible through original, alias AND shallow
    return original, shallow, deep
