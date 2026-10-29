"""Object lifecycle in Python: context managers, weak references and GC."""
from __future__ import annotations

import gc
import weakref
from contextlib import contextmanager


class Connection:
    """Resource that must be closed: implement the context-manager protocol."""

    open_count = 0                     # class-level counter of open connections

    def __init__(self, name: str) -> None:
        self.name = name
        self.closed = False
        Connection.open_count += 1

    def close(self) -> None:
        if not self.closed:
            self.closed = True
            Connection.open_count -= 1

    def __enter__(self) -> "Connection":   # called at the start of `with`
        return self

    def __exit__(self, exc_type, exc, tb) -> bool:  # ALWAYS called, even on exceptions
        self.close()
        return False                       # False = don't swallow the exception


@contextmanager
def transaction(log: list[str]):
    """Generator-based context manager: code before yield = enter, after = exit."""
    log.append("begin")
    try:
        yield
        log.append("commit")
    except Exception:
        log.append("rollback")
        raise


class Node:
    def __init__(self) -> None:
        self.other: "Node | None" = None


def cycle_is_collected() -> bool:
    a, b = Node(), Node()
    a.other, b.other = b, a            # reference cycle: refcounts never reach 0 alone
    probe = weakref.ref(a)             # weak ref does not keep `a` alive
    del a, b
    gc.collect()                       # the cycle collector frees the pair
    return probe() is None
