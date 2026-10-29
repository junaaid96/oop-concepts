"""Interfaces (Protocols) vs abstract base classes."""
from __future__ import annotations

from abc import ABC, abstractmethod
from typing import Protocol, runtime_checkable


@runtime_checkable                  # allows isinstance() checks against the protocol
class Exporter(Protocol):
    """Interface: only a contract, no state, no implementation."""

    def export(self, rows: list[list[object]]) -> str: ...


class JsonExporter:                 # implements Exporter without inheriting from it
    def export(self, rows: list[list[object]]) -> str:
        import json
        return json.dumps(rows)


class Vehicle(ABC):
    """Abstract class: shared state + shared code + abstract hooks."""

    def __init__(self, brand: str) -> None:   # abstract classes CAN have constructors
        self.brand = brand

    @abstractmethod
    def wheels(self) -> int: ...              # hook every subclass must provide

    def summary(self) -> str:                 # template method: fixed algorithm, variable step
        return f"{self.brand} with {self.wheels()} wheels"


class Car(Vehicle):
    def wheels(self) -> int:
        return 4


class Bike(Vehicle):
    def wheels(self) -> int:
        return 2


def save_report(exporter: Exporter, rows: list[list[object]]) -> str:
    return exporter.export(rows)              # depends on the interface only
