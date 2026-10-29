"""The same problems solved in an object-oriented way and in a functional way."""
from __future__ import annotations

import math
from collections import Counter
from concurrent.futures import ThreadPoolExecutor
from dataclasses import dataclass, replace
from functools import reduce
from threading import Lock
from typing import Callable, Generic, Iterable, TypeVar, Union

T = TypeVar("T")
U = TypeVar("U")


@dataclass(frozen=True)
class Item:
    name: str
    price: float
    qty: int


# =============================== 1. Cart total ===================================
class Cart:                                        # OOP: state + behaviour together
    def __init__(self) -> None:
        self._items: list[Item] = []
        self._discount = 0.0

    def add(self, item: Item) -> None:             # mutates the cart
        self._items.append(item)

    def apply_discount(self, pct: float) -> None:
        self._discount = pct

    def total(self) -> float:
        subtotal = sum(i.price * i.qty for i in self._items)
        return round(subtotal * (1 - self._discount / 100), 2)


def line_total(item: Item) -> float:               # FP: small pure functions...
    return item.price * item.qty


def discount(pct: float) -> Callable[[float], float]:   # ...a higher-order function returning a function
    return lambda amount: amount * (1 - pct / 100)


def pipe(value, *fns):                             # compose left to right
    return reduce(lambda acc, f: f(acc), fns, value)


def cart_total(items: Iterable[Item], pct: float = 0) -> float:
    return pipe(sum(map(line_total, items)), discount(pct), lambda x: round(x, 2))


# =============================== 2. Shapes =========================================
class ShapeOOP:                                     # OOP: open to new TYPES
    def area(self) -> float:
        raise NotImplementedError


class CircleOOP(ShapeOOP):
    def __init__(self, r: float) -> None:
        self.r = r

    def area(self) -> float:
        return math.pi * self.r ** 2


class SquareOOP(ShapeOOP):
    def __init__(self, s: float) -> None:
        self.s = s

    def area(self) -> float:
        return self.s ** 2


@dataclass(frozen=True)
class Circle:                                       # FP: plain data...
    r: float


@dataclass(frozen=True)
class Square:
    s: float


Shape = Union[Circle, Square]


def area(shape: Shape) -> float:                    # ...and functions: open to new OPERATIONS
    match shape:
        case Circle(r):
            return math.pi * r ** 2
        case Square(s):
            return s ** 2
    raise TypeError(shape)


def perimeter(shape: Shape) -> float:               # adding an operation = one new function
    match shape:
        case Circle(r):
            return 2 * math.pi * r
        case Square(s):
            return 4 * s
    raise TypeError(shape)


# =============================== 3. Failure handling ===============================
class OrderError(Exception):
    pass


class OrderProcessorOOP:                            # OOP + exceptions + mutation
    def __init__(self, stock: dict[str, int]) -> None:
        self.stock = stock

    def place(self, name: str, qty: int) -> int:
        if qty <= 0:
            raise OrderError("qty must be positive")
        if self.stock.get(name, 0) < qty:
            raise OrderError("out of stock")
        self.stock[name] -= qty                     # side effect
        return self.stock[name]


@dataclass(frozen=True)
class Ok(Generic[T]):
    value: T

    def bind(self, fn: "Callable[[T], Result[U]]") -> "Result[U]":   # flat_map: chain steps
        return fn(self.value)

    def map(self, fn: Callable[[T], U]) -> "Result[U]":
        return Ok(fn(self.value))


@dataclass(frozen=True)
class Err:
    error: str

    def bind(self, fn) -> "Err":                    # short-circuits: later steps are skipped
        return self

    def map(self, fn) -> "Err":
        return self


Result = Union[Ok[T], Err]


def validate_qty(order: tuple[str, int]) -> Result[tuple[str, int]]:
    return Ok(order) if order[1] > 0 else Err("qty must be positive")


def check_stock(stock: dict[str, int]) -> Callable[[tuple[str, int]], Result[tuple[str, int]]]:
    return lambda o: Ok(o) if stock.get(o[0], 0) >= o[1] else Err("out of stock")


def place_order(stock: dict[str, int], name: str, qty: int) -> Result[dict[str, int]]:
    """Pure: returns a NEW stock dict (or an Err); the input is never mutated."""
    return (Ok((name, qty))
            .bind(validate_qty)
            .bind(check_stock(stock))
            .map(lambda o: {**stock, o[0]: stock[o[0]] - o[1]}))


# =============================== 4. Bank account ===================================
class Account:                                      # OOP: a live entity guarding its state
    def __init__(self) -> None:
        self._balance = 0.0

    def deposit(self, amt: float) -> None:
        self._balance += amt

    def withdraw(self, amt: float) -> None:
        if amt > self._balance:
            raise ValueError("insufficient funds")
        self._balance -= amt

    @property
    def balance(self) -> float:
        return self._balance


Event = tuple[str, float]


def apply_event(balance: float, event: Event) -> float:   # FP: state = fold(events)
    kind, amt = event
    return balance + amt if kind == "deposit" else balance - amt


def balance_from_events(events: Iterable[Event]) -> float:
    return reduce(apply_event, events, 0.0)


# =============================== 5. Strategy =========================================
class PricingStrategy:                              # OOP Strategy pattern
    def price(self, base: float) -> float:
        return base


class BlackFriday(PricingStrategy):
    def price(self, base: float) -> float:
        return base * 0.5


def checkout_oop(base: float, strategy: PricingStrategy) -> float:
    return strategy.price(base)


def checkout_fp(base: float, pricing: Callable[[float], float] = lambda b: b) -> float:
    return pricing(base)                            # the "strategy" is just a function


# =============================== 6. Parallel word count ============================
class SharedCounter:                                # OOP: shared mutable state needs a lock
    def __init__(self) -> None:
        self.counts: Counter[str] = Counter()
        self._lock = Lock()

    def add_text(self, text: str) -> None:
        words = text.split()
        with self._lock:                            # forget this and you get a race condition
            self.counts.update(words)


def count_words(text: str) -> Counter[str]:         # FP: pure function per chunk...
    return Counter(text.split())


def parallel_count(chunks: list[str]) -> Counter[str]:
    with ThreadPoolExecutor() as pool:
        partials = pool.map(count_words, chunks)    # ...no shared state, no locks
    return reduce(lambda a, b: a + b, partials, Counter())   # merge immutable results


# ---- hybrid: immutable object with "functional" update -----------------------------
@dataclass(frozen=True)
class Settings:
    theme: str = "light"
    font_size: int = 14

    def with_theme(self, theme: str) -> "Settings":   # object API, functional semantics
        return replace(self, theme=theme)
