"""Composition and delegation instead of inheritance."""
from __future__ import annotations

from typing import Protocol


class Engine(Protocol):
    def start(self) -> str: ...


class PetrolEngine:
    def start(self) -> str:
        return "vroom"


class ElectricEngine:
    def start(self) -> str:
        return "hum"


class Car:
    """A Car HAS an engine (composition). The engine can be swapped at runtime."""

    def __init__(self, engine: Engine) -> None:
        self.engine = engine            # the part is injected, not hard-wired

    def start(self) -> str:
        return f"car: {self.engine.start()}"   # delegation: forward the work to the part


class BadStack(list):
    """Inheritance misuse: exposes insert/sort/... which break the LIFO contract."""


class Stack:
    """Composition: wraps a list and exposes only stack operations."""

    def __init__(self) -> None:
        self._items: list = []

    def push(self, x) -> None:
        self._items.append(x)           # delegate to the list

    def pop(self):
        return self._items.pop()

    def __len__(self) -> int:
        return len(self._items)


class ReadOnlyView:
    """Automatic delegation with __getattr__, blocking mutating methods."""

    _blocked = {"append", "pop", "clear", "insert", "remove", "extend"}

    def __init__(self, target: list) -> None:
        self._target = target

    def __getattr__(self, name: str):   # only called when normal lookup fails
        if name in self._blocked:
            raise AttributeError(f"{name} is not allowed on a read-only view")
        return getattr(self._target, name)

    def __len__(self) -> int:
        return len(self._target)
