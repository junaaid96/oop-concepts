"""Anti-patterns side by side with their refactored versions."""
from __future__ import annotations

from dataclasses import dataclass, field


# ---- Anemic domain model (anti-pattern) -------------------------------------
@dataclass
class AnemicOrder:                         # just data, no rules
    items: list[float] = field(default_factory=list)
    status: str = "new"


class OrderService:                        # all logic lives elsewhere; easy to bypass
    @staticmethod
    def pay(order: AnemicOrder) -> None:
        order.status = "paid"              # nothing stops paying an empty order


# ---- Rich domain model (fix) --------------------------------------------------
class RichOrder:
    def __init__(self) -> None:
        self._items: list[float] = []
        self._status = "new"

    @property
    def status(self) -> str:
        return self._status

    def add(self, price: float) -> None:
        if self._status != "new":
            raise ValueError("cannot modify a paid order")
        self._items.append(price)

    def pay(self) -> None:                 # behaviour lives with the data it protects
        if not self._items:
            raise ValueError("cannot pay an empty order")
        if self._status != "new":
            raise ValueError("already paid")
        self._status = "paid"

    @property
    def total(self) -> float:
        return sum(self._items)


# ---- Deep inheritance (anti-pattern) vs composition (fix) --------------------
class Animal:
    def sound(self) -> str: return "..."
class Mammal(Animal): pass
class Canine(Mammal): pass
class Dog(Canine): pass
class RobotDog(Dog):                       # robot is not an animal: hierarchy breaks down
    def sound(self) -> str: return "beep-woof"


@dataclass
class Creature:                            # flat: behaviour assembled from parts
    name: str
    sound: str
    can_walk: bool = True

    def speak(self) -> str:
        return f"{self.name}: {self.sound}"


# ---- God object (anti-pattern) vs split responsibilities (fix) -----------------
class GodApp:
    """Validates, stores, emails and reports — four reasons to change."""
    def __init__(self) -> None:
        self.users: list[str] = []
        self.outbox: list[str] = []
    def register(self, email: str) -> None:
        if "@" not in email: raise ValueError
        self.users.append(email)
        self.outbox.append(f"welcome {email}")
    def report(self) -> str:
        return f"{len(self.users)} users"


class EmailValidator:
    def check(self, email: str) -> None:
        if "@" not in email:
            raise ValueError("invalid email")


class UserStore:
    def __init__(self) -> None:
        self.users: list[str] = []


class Mailer:
    def __init__(self) -> None:
        self.outbox: list[str] = []
    def welcome(self, email: str) -> None:
        self.outbox.append(f"welcome {email}")


class Registration:                        # thin coordinator
    def __init__(self, v: EmailValidator, store: UserStore, mail: Mailer) -> None:
        self.v, self.store, self.mail = v, store, mail

    def register(self, email: str) -> None:
        self.v.check(email)
        self.store.users.append(email)
        self.mail.welcome(email)
