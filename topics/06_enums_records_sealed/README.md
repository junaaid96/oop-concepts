# 06 · Enums, Records (Data Classes) & Sealed Types

> Files: [`modeling.py`](modeling.py) · [`modeling.cpp`](modeling.cpp) · tests: [`test_modeling.py`](test_modeling.py)

- **Enum**: a closed set of named constants, optionally with data and behaviour.
  Python `enum.Enum` / `StrEnum` / `Flag`; C++ `enum class` (scoped, no implicit int conversion).
- **Record / data class**: a class that is "just data" with generated equality, hashing and repr.
  Python `@dataclass(frozen=True, slots=True)`, `NamedTuple`; C++ aggregate `struct` with `= default` comparisons.
  (Java's `record` is the same idea.)
- **Sealed hierarchy / sum type**: a *closed* set of variants, so the compiler/type checker can verify
  every case is handled. Python: a `Union` of dataclasses + `match` + `assert_never`;
  C++: `std::variant` + `std::visit`. (Java: `sealed interface ... permits`.)

This is where OOP meets **algebraic data types** from functional programming: records are
*product types*, sealed hierarchies are *sum types*.

## Interview questions
1. Why is `enum class` safer than a plain C enum?
2. Open class hierarchy (virtual methods) vs closed sum type (variant/match): trade-offs? *Open: easy to add types, hard to add operations. Closed: easy to add operations, hard to add types (the "expression problem").*
3. What does `frozen=True` give a dataclass? *Immutability + `__hash__`.*

## Exercises
1. Add a `Triangle` variant to `Shape` and update `area`. What warns you if you forget?
2. Give `OrderStatus` a method `can_cancel()`.

<details><summary>Solutions</summary>

```python
@dataclass(frozen=True)
class Triangle: base: float; height: float
Shape = Circle | Square | Triangle
# in area(): case Triangle(b, h): return 0.5 * b * h
# assert_never(...) makes mypy/pyright report the missing case.

def can_cancel(self): return self in (OrderStatus.PENDING, OrderStatus.PAID)
```
</details>
