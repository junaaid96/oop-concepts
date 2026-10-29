# 05 · Generics (Parametric Polymorphism)

> Files: [`generics.py`](generics.py) · [`generics.cpp`](generics.cpp) · tests: [`test_generics.py`](test_generics.py)

Write code **once** that works for many types while keeping type safety.

- **Python**: `TypeVar`, `Generic[T]`, PEP 695 syntax `class Box[T]:` / `def first[T](xs: list[T]) -> T` (3.12+),
  bounds (`T: Comparable`), `Protocol` constraints. Generics are checked by tools like mypy/pyright,
  not at runtime.
- **C++**: templates are instantiated at compile time (zero runtime cost). C++20 **concepts**
  constrain template parameters with readable errors.
- **Variance**: `list[Cat]` is not a `list[Animal]` (mutable containers are invariant);
  `Sequence[Cat]` *is* a `Sequence[Animal]` (read-only → covariant).

## Interview questions
1. Generics vs `object`/`void*`: what do you gain? *Compile-time type safety, no casts.*
2. Explain covariance and contravariance with an example.
3. C++ templates vs Java generics? *Templates generate code per type (monomorphisation); Java erases types.*
4. What is a bounded type parameter / concept?

## Exercises
1. Write a generic `max_by(items, key)` function.
2. Write a C++ concept `Addable` and a generic `sum` function.

<details><summary>Solutions</summary>

```python
def max_by[T, K](items: list[T], key: Callable[[T], K]) -> T:
    return max(items, key=key)
```
```cpp
template <typename T> concept Addable = requires(T a, T b) { { a + b } -> std::same_as<T>; };
template <Addable T> T sum(const std::vector<T>& v, T init) { for (auto& x : v) init = init + x; return init; }
```
</details>
