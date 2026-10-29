"""Dependency injection: constructor, setter and parameter injection + a tiny container."""
from __future__ import annotations

from datetime import datetime
from typing import Callable, Protocol


class Clock(Protocol):
    def now(self) -> datetime: ...


class SystemClock:
    def now(self) -> datetime:
        return datetime.now()


class FixedClock:                              # test double: deterministic time
    def __init__(self, at: datetime) -> None:
        self.at = at

    def now(self) -> datetime:
        return self.at


class UserRepo(Protocol):
    def find(self, user_id: int) -> str | None: ...


class InMemoryUserRepo:
    def __init__(self, users: dict[int, str]) -> None:
        self.users = users

    def find(self, user_id: int) -> str | None:
        return self.users.get(user_id)


class GreetingService:
    def __init__(self, repo: UserRepo, clock: Clock) -> None:   # constructor injection
        self.repo = repo
        self.clock = clock
        self.log: Callable[[str], None] = lambda msg: None      # optional: setter injection

    def greet(self, user_id: int, fmt: Callable[[str], str] = str) -> str:  # parameter injection
        name = self.repo.find(user_id) or "guest"
        part = "morning" if self.clock.now().hour < 12 else "evening"
        msg = fmt(f"Good {part}, {name}")
        self.log(msg)
        return msg


class Container:
    """Minimal DI container: register factories, resolve singletons lazily."""

    def __init__(self) -> None:
        self._factories: dict[type, Callable[["Container"], object]] = {}
        self._instances: dict[type, object] = {}

    def register(self, key: type, factory: Callable[["Container"], object]) -> None:
        self._factories[key] = factory

    def resolve(self, key: type):
        if key not in self._instances:
            self._instances[key] = self._factories[key](self)
        return self._instances[key]


def composition_root() -> GreetingService:
    """The ONE place that knows the concrete classes."""
    c = Container()
    c.register(UserRepo, lambda _: InMemoryUserRepo({1: "Ana"}))
    c.register(Clock, lambda _: SystemClock())
    c.register(GreetingService, lambda c: GreetingService(c.resolve(UserRepo), c.resolve(Clock)))
    return c.resolve(GreetingService)
