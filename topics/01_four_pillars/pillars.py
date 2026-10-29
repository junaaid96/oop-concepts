"""The four pillars of OOP with runnable Python examples.

Run:  python3 pillars.py
Test: python3 -m unittest test_pillars.py
"""
from __future__ import annotations

import math
from abc import ABC, abstractmethod
from functools import singledispatch
from typing import Generic, Protocol, TypeVar


# ---------------------------------------------------------------- Encapsulation
class BankAccount:
    """Keeps its balance private and only changes it through validated behaviour."""

    def __init__(self, owner: str) -> None:
        self.owner = owner          # public: harmless to read/write
        self._balance = 0.0         # "protected" by convention: don't touch from outside

    @property
    def balance(self) -> float:     # read-only view of the state (no setter defined)
        return self._balance

    def deposit(self, amount: float) -> None:
        if amount <= 0:             # guard the invariant "deposits are positive"
            raise ValueError("deposit must be positive")
        self._balance += amount

    def withdraw(self, amount: float) -> None:
        if amount > self._balance:  # guard the invariant "balance never negative"
            raise ValueError("insufficient funds")
        self._balance -= amount


# ------------------------------------------------- Abstraction + subtype polymorphism
class Shape(ABC):
    """Abstract base: clients only need to know a shape has an area."""

    @abstractmethod
    def area(self) -> float: ...    # subclasses MUST implement this

    def describe(self) -> str:      # template method reusing the abstract one
        return f"{type(self).__name__} with area {self.area():.2f}"


class Circle(Shape):                # inheritance: a Circle *is a* Shape
    def __init__(self, r: float) -> None:
        self.r = r

    def area(self) -> float:        # overriding = dynamic (runtime) polymorphism
        return math.pi * self.r ** 2


class Rectangle(Shape):
    def __init__(self, w: float, h: float) -> None:
        self.w, self.h = w, h

    def area(self) -> float:
        return self.w * self.h


def total_area(shapes: list[Shape]) -> float:
    """Works with ANY current or future Shape: the call is bound at runtime."""
    return sum(s.area() for s in shapes)


# --------------------------------------------------------- Ad hoc polymorphism
class Vector2:
    """Operator overloading: + and == mean something sensible for vectors."""

    def __init__(self, x: float, y: float) -> None:
        self.x, self.y = x, y

    def __add__(self, other: "Vector2") -> "Vector2":   # v1 + v2
        return Vector2(self.x + other.x, self.y + other.y)

    def __eq__(self, other: object) -> bool:           # v1 == v2
        return isinstance(other, Vector2) and (self.x, self.y) == (other.x, other.y)

    def __repr__(self) -> str:
        return f"Vector2({self.x}, {self.y})"


@singledispatch
def to_text(value) -> str:          # fallback implementation
    return str(value)


@to_text.register
def _(value: float) -> str:         # chosen when the argument is a float
    return f"{value:.2f}"


@to_text.register
def _(value: list) -> str:          # chosen when the argument is a list
    return ", ".join(to_text(v) for v in value)


# --------------------------------------------------------- Parametric polymorphism
T = TypeVar("T")


class Stack(Generic[T]):
    """One implementation, any element type."""

    def __init__(self) -> None:
        self._items: list[T] = []

    def push(self, item: T) -> None:
        self._items.append(item)

    def pop(self) -> T:
        return self._items.pop()

    def __len__(self) -> int:
        return len(self._items)


# --------------------------------------------------------- Duck typing / Protocol
class Speaker(Protocol):            # structural type: anything with speak() fits
    def speak(self) -> str: ...


class Duck:
    def speak(self) -> str:
        return "Quack"


class Robot:                        # NOT related to Duck by inheritance
    def speak(self) -> str:
        return "Beep"


def chorus(speakers: list[Speaker]) -> str:
    return " ".join(s.speak() for s in speakers)


if __name__ == "__main__":
    acc = BankAccount("Ana")
    acc.deposit(100)
    acc.withdraw(30)
    print("balance:", acc.balance)
    print(total_area([Circle(1), Rectangle(2, 3)]))
    print(Vector2(1, 2) + Vector2(3, 4))
    print(to_text([1.5, 2, "x"]))
    s: Stack[int] = Stack()
    s.push(1)
    print("stack size:", len(s))
    print(chorus([Duck(), Robot()]))
