# 02 · Classes, Objects, Constructors, Static, Final & Immutability

> Files: [`classes.py`](classes.py) · [`classes.cpp`](classes.cpp) · tests: [`test_classes.py`](test_classes.py)

- **Class** = blueprint (fields + methods). **Object** = an instance with its own state and identity.
- **Constructor** puts a new object into a *valid* state. In C++ prefer member-initializer lists; mark
  single-argument constructors `explicit`. In Python `__init__` initialises, `__new__` allocates.
- **Alternative constructors**: Python `@classmethod` (`Date.from_iso("2026-01-01")`); C++ static factory functions.
- **Static members** belong to the class, not to an instance (counters, factories, constants).
  Python: class attributes, `@staticmethod`, `@classmethod`. C++: `static` members / `inline static`.
- **final / const**: C++ `const` members & methods, `final` classes/methods; Python `typing.Final`
  (checked by type checkers only) and `@final`.
- **Immutability**: an object whose state cannot change after construction. Benefits: thread-safe,
  safe to share and cache, usable as dict/set keys, easier reasoning. Python: `@dataclass(frozen=True)`,
  `NamedTuple`; C++: `const` members, return new objects from "wither" methods.

## Rule of zero / three / five (C++)
If a class manages a resource it needs a destructor, copy ctor/assign, move ctor/assign (five).
Best: let members like `std::string`, `std::vector`, `std::unique_ptr` manage resources so you need **zero**.

## Interview questions
1. Difference between `@staticmethod` and `@classmethod`? *classmethod receives the class (good for alternative constructors that respect subclasses); staticmethod receives nothing.*
2. Why make a constructor `explicit` in C++?
3. What are the benefits of immutable objects? Any costs? *More allocations/copies.*
4. Is a Python class attribute shared between instances? What happens with a mutable default like a list?
5. What is the rule of five?

## Exercises
1. Add `Money.__mul__(factor)` returning a new `Money`.
2. Give `Temperature` a `from_fahrenheit` class method.
3. Count how many `Counter` objects are alive using a class attribute.

<details><summary>Solutions</summary>

```python
def __mul__(self, factor):            # 1 (inside Money)
    return Money(round(self.amount * factor, 2), self.currency)

@classmethod                          # 2 (inside Temperature)
def from_fahrenheit(cls, f): return cls((f - 32) * 5 / 9)

class Counter:                        # 3
    alive = 0
    def __init__(self): Counter.alive += 1
    def __del__(self): Counter.alive -= 1
```
</details>
