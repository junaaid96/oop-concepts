"""Enums, records (dataclasses) and sealed sum types with pattern matching."""
from __future__ import annotations

import math
from dataclasses import dataclass
from enum import Enum
from typing import Union

try:                                    # assert_never is in typing from Python 3.11
    from typing import assert_never
except ImportError:                     # pragma: no cover
    def assert_never(x):                # type: ignore[misc]
        raise AssertionError(f"unhandled: {x!r}")


class OrderStatus(Enum):
    PENDING = "pending"
    PAID = "paid"
    SHIPPED = "shipped"
    CANCELLED = "cancelled"

    def is_final(self) -> bool:         # enums can carry behaviour
        return self in (OrderStatus.SHIPPED, OrderStatus.CANCELLED)


@dataclass(frozen=True, slots=True)     # a "record": immutable, compact, eq/hash/repr generated
class Point:
    x: float
    y: float


@dataclass(frozen=True)
class Circle:
    radius: float


@dataclass(frozen=True)
class Square:
    side: float


Shape = Union[Circle, Square]           # closed set of variants = sum type


def area(shape: Shape) -> float:
    match shape:                        # structural pattern matching (3.10+)
        case Circle(radius=r):
            return math.pi * r * r
        case Square(side=s):
            return s * s
        case _:
            assert_never(shape)         # type checker errors if a variant is unhandled
