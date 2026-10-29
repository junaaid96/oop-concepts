"""Common design patterns in idiomatic Python."""
from __future__ import annotations

from collections import defaultdict
from dataclasses import dataclass, field
from typing import Callable, Protocol


# ---- Strategy -----------------------------------------------------------------
class ShippingStrategy(Protocol):
    def cost(self, kg: float) -> float: ...


class Standard:
    def cost(self, kg: float) -> float:
        return 5 + 1 * kg


class Free:
    def cost(self, kg: float) -> float:
        return 0.0


class Order:
    def __init__(self, kg: float, shipping: ShippingStrategy) -> None:
        self.kg, self.shipping = kg, shipping       # algorithm injected, swappable

    def shipping_cost(self) -> float:
        return self.shipping.cost(self.kg)


# ---- Factory + Decorator ---------------------------------------------------------
class Notifier(Protocol):
    def send(self, msg: str) -> str: ...


class EmailNotifier:
    def send(self, msg: str) -> str:
        return f"email:{msg}"


class SmsNotifier:
    def send(self, msg: str) -> str:
        return f"sms:{msg}"


def make_notifier(kind: str) -> Notifier:
    """Factory: callers ask for a kind, not a concrete class."""
    registry = {"email": EmailNotifier, "sms": SmsNotifier}
    return registry[kind]()


class LoggingNotifier:
    """Decorator: same interface, wraps another notifier and adds logging."""

    def __init__(self, inner: Notifier, log: list[str]) -> None:
        self.inner, self.log = inner, log

    def send(self, msg: str) -> str:
        self.log.append(f"sending {msg}")
        return self.inner.send(msg)


# ---- Builder ---------------------------------------------------------------------
@dataclass(frozen=True)
class Pizza:
    size: str
    toppings: tuple[str, ...]


class PizzaBuilder:
    def __init__(self) -> None:
        self._size = "medium"
        self._toppings: list[str] = []

    def size(self, s: str) -> "PizzaBuilder":       # returns self for a fluent API
        self._size = s
        return self

    def topping(self, t: str) -> "PizzaBuilder":
        self._toppings.append(t)
        return self

    def build(self) -> Pizza:
        return Pizza(self._size, tuple(self._toppings))


# ---- Adapter -----------------------------------------------------------------------
class LegacyPrinter:
    def print_upper(self, text: str) -> str:       # old, incompatible API
        return text.upper()


class LegacyPrinterAdapter:
    """Adapts LegacyPrinter to the Notifier interface."""

    def __init__(self, legacy: LegacyPrinter) -> None:
        self.legacy = legacy

    def send(self, msg: str) -> str:
        return self.legacy.print_upper(msg)


# ---- Observer ------------------------------------------------------------------------
class EventBus:
    def __init__(self) -> None:
        self._subs: dict[str, list[Callable[[object], None]]] = defaultdict(list)

    def subscribe(self, event: str, handler: Callable[[object], None]) -> None:
        self._subs[event].append(handler)

    def publish(self, event: str, payload: object) -> None:
        for h in self._subs[event]:                  # notify every subscriber
            h(payload)


# ---- Command (with undo) -----------------------------------------------------------
@dataclass
class Document:
    text: str = ""
    history: list["AddText"] = field(default_factory=list)

    def run(self, cmd: "AddText") -> None:
        cmd.execute(self)
        self.history.append(cmd)

    def undo(self) -> None:
        self.history.pop().undo(self)


@dataclass
class AddText:
    value: str

    def execute(self, doc: Document) -> None:
        doc.text += self.value

    def undo(self, doc: Document) -> None:
        doc.text = doc.text[: -len(self.value)]
