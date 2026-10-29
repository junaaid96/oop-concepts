"""Design by contract with small decorators for pre/postconditions and invariants."""
from __future__ import annotations

import functools
from typing import Callable


class ContractError(AssertionError):
    pass


def requires(pred: Callable[..., bool], msg: str):
    """Precondition: checked before the call with the same arguments."""
    def deco(fn):
        @functools.wraps(fn)
        def wrapper(*args, **kwargs):
            if not pred(*args, **kwargs):
                raise ContractError(f"precondition failed: {msg}")
            return fn(*args, **kwargs)
        return wrapper
    return deco


def ensures(pred: Callable[..., bool], msg: str):
    """Postcondition: pred(result, *args) checked after the call."""
    def deco(fn):
        @functools.wraps(fn)
        def wrapper(*args, **kwargs):
            result = fn(*args, **kwargs)
            if not pred(result, *args, **kwargs):
                raise ContractError(f"postcondition failed: {msg}")
            return result
        return wrapper
    return deco


def invariant(check: Callable[[object], bool], msg: str):
    """Class decorator: re-check the invariant after every public method."""
    def deco(cls):
        for name, attr in list(vars(cls).items()):
            if callable(attr) and not name.startswith("_"):
                def make(method):
                    @functools.wraps(method)
                    def wrapper(self, *a, **kw):
                        out = method(self, *a, **kw)
                        if not check(self):
                            raise ContractError(f"invariant broken: {msg}")
                        return out
                    return wrapper
                setattr(cls, name, make(attr))
        return cls
    return deco


@invariant(lambda self: self.balance >= 0, "balance >= 0")
class Account:
    def __init__(self) -> None:
        self.balance = 0

    @requires(lambda self, amount: amount > 0, "amount > 0")
    @ensures(lambda result, self, amount: result == self.balance, "returns new balance")
    def deposit(self, amount: int) -> int:
        self.balance += amount
        return self.balance

    def unsafe_set(self, value: int) -> None:   # only here to show the invariant firing
        self.balance = value
