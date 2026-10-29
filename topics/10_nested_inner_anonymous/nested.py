"""Nested classes, 'inner' classes, local classes, anonymous objects and closures."""
from __future__ import annotations

from typing import Callable, Iterator


class LinkedList:
    class _Node:                         # nested class: an implementation detail
        __slots__ = ("value", "next")

        def __init__(self, value, next_=None) -> None:
            self.value = value
            self.next = next_

    def __init__(self) -> None:
        self._head: LinkedList._Node | None = None

    def push_front(self, value) -> None:
        self._head = LinkedList._Node(value, self._head)

    def __iter__(self) -> Iterator:      # generator replaces a hand-written inner iterator class
        node = self._head
        while node:
            yield node.value
            node = node.next


class Button:
    class Clicker:                       # "inner" class: explicitly bound to an outer instance
        def __init__(self, outer: "Button") -> None:
            self.outer = outer

        def click(self) -> None:
            self.outer.clicks += 1

    def __init__(self) -> None:
        self.clicks = 0

    def clicker(self) -> "Button.Clicker":
        return Button.Clicker(self)


def make_validator(min_len: int) -> Callable[[str], bool]:
    class LengthRule:                    # local class: visible only in this function
        def __call__(self, s: str) -> bool:
            return len(s) >= min_len     # closes over the function's local variable
    return LengthRule()


def anonymous_greeter():
    # "anonymous class": build a one-off type at runtime
    return type("Greeter", (), {"greet": lambda self, n: f"hi {n}"})()
