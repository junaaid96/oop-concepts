# 01 · The Four Pillars, Deepened

> Files: [`pillars.py`](pillars.py) · [`pillars.cpp`](pillars.cpp) · tests: [`test_pillars.py`](test_pillars.py)

## 1. Encapsulation
Bundle data with the methods that operate on it, and **protect invariants** by hiding state.
It is *not* "make every field private and add getters/setters" — that just moves the problem.
Good encapsulation exposes **behaviour** (`account.withdraw(50)`) rather than raw state (`account.balance -= 50`).

| Language | Mechanism |
|---|---|
| C++ | `private` / `protected` / `public`, `friend` (use sparingly) |
| Python | convention `_name`, name mangling `__name`, `@property` for computed/validated access |

## 2. Abstraction
Show *what* an object does, hide *how*. Clients program against a small, stable surface
(an interface / abstract base class) and the details can change freely.

## 3. Inheritance
An "is-a" relationship that reuses and specialises behaviour. Prefer **shallow** hierarchies;
use it when the subclass truly is substitutable for the base (Liskov Substitution Principle).
See [04_composition_delegation](../04_composition_delegation) for the usual alternative.

## 4. Polymorphism — the four kinds
| Kind | Also called | C++ | Python |
|---|---|---|---|
| **Subtype (dynamic)** | runtime / late binding | `virtual` + override | every method call is dynamic |
| **Ad hoc (static)** | overloading, operator overloading | function/operator overloading | `functools.singledispatch`, dunder methods (`__add__`) |
| **Parametric** | generics | templates | `TypeVar`, `Generic[T]` |
| **Coercion** | implicit conversion | converting constructors | `int + float -> float` |

Python also has **duck typing**: "if it quacks like a duck…" — no shared base needed, and
`typing.Protocol` lets you type-check that structurally.

## Common mistakes
- Exposing setters for everything (breaks encapsulation).
- Using inheritance just to reuse code (use composition).
- Forgetting a `virtual` destructor in a C++ polymorphic base (UB when deleting via base pointer).
- Overloading in Python by redefining a method (the last definition silently wins).

## Interview questions
1. What is the difference between encapsulation and abstraction? *Encapsulation hides state and guards invariants; abstraction hides complexity behind a simple contract.*
2. Name the kinds of polymorphism with an example of each.
3. Why does a polymorphic C++ base class need a virtual destructor?
4. How does Python achieve polymorphism without `virtual`?
5. What is object slicing in C++ and how do you avoid it? *Copying a derived object into a base by value drops the derived part; pass by reference/pointer.*

## Exercises
1. Add a `Triangle` shape that works with `total_area` without changing `total_area`.
2. Make `BankAccount` reject withdrawals that exceed the balance plus an overdraft limit.
3. Write a `singledispatch` function `describe` that handles `int`, `str` and `list` differently.

<details><summary>Solutions</summary>

```python
# 1. Open/closed in action: only a new class is needed.
class Triangle(Shape):
    def __init__(self, base, height): self.base, self.height = base, height
    def area(self): return 0.5 * self.base * self.height

# 2.
class OverdraftAccount(BankAccount):
    def __init__(self, owner, limit): super().__init__(owner); self._limit = limit
    def withdraw(self, amount):
        if amount > self.balance + self._limit: raise ValueError("limit exceeded")
        self._balance -= amount

# 3.
from functools import singledispatch
@singledispatch
def describe(x): return "something"
@describe.register
def _(x: int): return "an int"
@describe.register
def _(x: str): return "a string"
@describe.register
def _(x: list): return f"a list of {len(x)}"
```
</details>
