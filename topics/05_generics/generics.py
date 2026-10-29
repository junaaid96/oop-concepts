"""Generic classes and functions in Python (works on 3.9+, PEP 695 syntax shown in README)."""
from __future__ import annotations

from dataclasses import dataclass
from typing import Callable, Generic, Optional, Protocol, TypeVar

T = TypeVar("T")                     # any type
U = TypeVar("U")


class SupportsLessThan(Protocol):
    def __lt__(self, other, /) -> bool: ...


C = TypeVar("C", bound=SupportsLessThan)   # bounded: must support <


@dataclass
class Box(Generic[T]):
    """A container that remembers the type of what it holds."""
    value: T

    def map(self, fn: Callable[[T], U]) -> "Box[U]":   # Box[int] -> Box[str] etc.
        return Box(fn(self.value))


def first(items: list[T]) -> Optional[T]:
    """Return the first item or None — the return type follows the input type."""
    return items[0] if items else None


def smallest(items: list[C]) -> C:
    """Only accepts element types that can be compared with <."""
    result = items[0]
    for x in items[1:]:
        if x < result:
            result = x
    return result


class Repository(Generic[T]):
    """Generic in-memory repository: one implementation for Users, Orders, ..."""

    def __init__(self) -> None:
        self._items: dict[int, T] = {}

    def add(self, id_: int, item: T) -> None:
        self._items[id_] = item

    def get(self, id_: int) -> Optional[T]:
        return self._items.get(id_)
