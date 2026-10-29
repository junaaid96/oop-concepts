# 07 · Value vs Reference, Identity vs Equality, Hashing

> Files: [`equality.py`](equality.py) · [`equality.cpp`](equality.cpp) · tests: [`test_equality.py`](test_equality.py)

- **Identity**: *the same object* (`a is b` in Python, same address in C++).
- **Equality**: *equivalent value* (`a == b`, defined by `__eq__` / `operator==`).
- **Value semantics** (C++ default, Python ints/strs/tuples): assignment copies the value.
  **Reference semantics** (Python objects, C++ pointers/references): assignment shares one object —
  mutations are visible through every alias.
- **Hash contract**: if `a == b` then `hash(a) == hash(b)`. Break it and objects "disappear" in sets/dicts.
  Mutable objects used as keys are dangerous: change a field, the hash changes, the lookup fails.
- **Entities** are compared by identity/ID; **value objects** by all their fields.

## Interview questions
1. `==` vs `is` in Python? Why does `a is b` sometimes work for small ints?
2. Why must `__hash__` be consistent with `__eq__`? What does Python do if you define only `__eq__`? *Sets `__hash__ = None` → unhashable.*
3. Shallow vs deep copy?
4. In C++, what does the defaulted `operator<=>` give you?

## Exercises
1. Make `Customer` hashable by `id` only.
2. Show the bug when a mutable list default is shared between instances, then fix it.

<details><summary>Solutions</summary>

```python
def __hash__(self): return hash(self.id)

class Cart:
    def __init__(self, items=None): self.items = [] if items is None else items   # not items=[]
```
</details>
