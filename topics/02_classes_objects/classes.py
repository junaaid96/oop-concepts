"""Classes, objects, constructors, static members and immutability in Python."""
from __future__ import annotations

from dataclasses import dataclass, replace
from typing import ClassVar, Final

MAX_USERS: Final = 100            # Final: type checkers forbid reassignment


class User:
    count: ClassVar[int] = 0      # class (static) attribute shared by all instances

    def __init__(self, name: str, email: str) -> None:   # constructor: make a valid object
        if "@" not in email:
            raise ValueError("invalid email")
        self.name = name          # instance attributes: one copy per object
        self.email = email
        User.count += 1           # update the shared counter

    @classmethod
    def from_csv(cls, line: str) -> "User":   # alternative constructor; cls respects subclasses
        name, email = line.split(",")
        return cls(name.strip(), email.strip())

    @staticmethod
    def is_valid_name(name: str) -> bool:     # utility: needs neither instance nor class
        return name.isalpha()


@dataclass(frozen=True)           # frozen: assignment after __init__ raises FrozenInstanceError
class Money:
    amount: float
    currency: str = "USD"

    def __post_init__(self) -> None:          # runs after the generated __init__ to validate
        if self.amount < 0:
            raise ValueError("amount must be >= 0")

    def add(self, other: "Money") -> "Money":  # immutable: returns a NEW object
        if other.currency != self.currency:
            raise ValueError("currency mismatch")
        return Money(self.amount + other.amount, self.currency)

    def with_currency(self, currency: str) -> "Money":   # a "wither"
        return replace(self, currency=currency)


class Temperature:
    def __init__(self, celsius: float) -> None:
        self._celsius = celsius

    @property
    def fahrenheit(self) -> float:            # computed attribute
        return self._celsius * 9 / 5 + 32


if __name__ == "__main__":
    u = User.from_csv("Ana, ana@example.com")
    print(u.name, User.count, User.is_valid_name("Ana"))
    print(Money(10).add(Money(5)))
    print(Temperature(100).fahrenheit)
