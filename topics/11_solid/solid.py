"""SOLID principles applied to a small invoicing feature."""
from __future__ import annotations

from dataclasses import dataclass
from typing import Protocol


@dataclass(frozen=True)
class LineItem:
    name: str
    price: float
    qty: int


# S — each class has ONE reason to change -------------------------------------
class InvoiceCalculator:                      # changes only if pricing rules change
    def __init__(self, discounts: list["Discount"] | None = None) -> None:
        self.discounts = discounts or []

    def total(self, items: list[LineItem]) -> float:
        subtotal = sum(i.price * i.qty for i in items)
        for d in self.discounts:              # O — new discounts plug in, no edits here
            subtotal = d.apply(subtotal)
        return round(subtotal, 2)


class InvoiceFormatter:                       # changes only if the output format changes
    def format(self, items: list[LineItem], total: float) -> str:
        lines = [f"{i.name} x{i.qty}" for i in items]
        return "\n".join(lines + [f"TOTAL {total:.2f}"])


# O — extension points ----------------------------------------------------------
class Discount(Protocol):
    def apply(self, total: float) -> float: ...


class FlatDiscount:
    def __init__(self, amount: float) -> None:
        self.amount = amount

    def apply(self, total: float) -> float:
        return max(0.0, total - self.amount)


# L — substitutable subtypes ------------------------------------------------------
class Bird:
    def move(self) -> str:                    # contract: every bird can MOVE (not fly)
        return "moves"


class Sparrow(Bird):
    def move(self) -> str:
        return "flies"


class Penguin(Bird):                          # no "fly() raises NotImplementedError" trap
    def move(self) -> str:
        return "swims"


# I — small interfaces --------------------------------------------------------------
class Sender(Protocol):
    def send(self, to: str, body: str) -> None: ...


# D — high-level policy depends on the abstraction, the detail is injected ---------
class InvoiceService:
    def __init__(self, calc: InvoiceCalculator, fmt: InvoiceFormatter, sender: Sender) -> None:
        self.calc, self.fmt, self.sender = calc, fmt, sender

    def bill(self, customer: str, items: list[LineItem]) -> float:
        total = self.calc.total(items)
        self.sender.send(customer, self.fmt.format(items, total))
        return total


class FakeSender:                             # test double satisfies the interface
    def __init__(self) -> None:
        self.sent: list[tuple[str, str]] = []

    def send(self, to: str, body: str) -> None:
        self.sent.append((to, body))
