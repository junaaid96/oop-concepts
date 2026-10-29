# 10 · Nested, Inner, Local & Anonymous Classes (and Lambdas)

> Files: [`nested.py`](nested.py) · [`nested.cpp`](nested.cpp) · tests: [`test_nested.py`](test_nested.py)

| Kind | Meaning | C++ | Python |
|---|---|---|---|
| Nested (static) class | a class scoped inside another, no link to an outer instance | `class Outer { class Node {}; };` | `class Outer: class Node:` |
| Inner class | holds a reference to an enclosing *instance* (Java non-static inner class) | pass the outer pointer explicitly | pass `self` explicitly |
| Local class | defined inside a function | supported | supported (and can close over locals) |
| Anonymous class | one-off implementation of an interface | lambda / unnamed struct | `lambda`, `type(...)`, or a closure |

Use them to **hide helper types** (iterators, nodes, builders) and keep the public API small.
Lambdas/closures replaced most anonymous classes once languages added first-class functions.

## Interview questions
1. Nested static class vs inner class in Java — and how do you emulate each in C++/Python?
2. What is a closure? What does it capture in C++ (`[=]`, `[&]`, `[this]`)?
3. Why is a nested `Node` class a good encapsulation choice for a linked list?

## Exercises
1. Add `__len__` to `LinkedList` using its nested `Node`.
2. Write a closure `make_counter()` returning a function that increments and returns a count.

<details><summary>Solutions</summary>

```python
def __len__(self): return sum(1 for _ in self)
def make_counter():
    n = 0
    def inc():
        nonlocal n; n += 1; return n
    return inc
```
</details>
