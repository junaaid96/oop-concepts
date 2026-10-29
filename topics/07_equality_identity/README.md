# 07 · Value vs Reference, Identity vs Equality, Hashing

> Files: [`Equality.java`](src/main/java/oop/equality/Equality.java) · [`equality.cpp`](equality.cpp) · tests: [`EqualityTest.java`](src/test/java/oop/equality/EqualityTest.java)

- **Identity**: *the same object* (`a == b` on references in Java, same address in C++).
- **Equality**: *equivalent value* (`a.equals(b)` in Java, `operator==` in C++).
- **Value semantics** (C++ default, Java primitives): assignment copies the value.
  **Reference semantics** (every Java object, C++ pointers/references): assignment shares one object —
  mutations are visible through every alias.
- **Hash contract**: if `a.equals(b)` then `a.hashCode() == b.hashCode()`. Break it (see `BrokenKey`) and objects "disappear" in `HashSet`/`HashMap`.
  Mutable objects used as keys are dangerous: change a field, the hash changes, the lookup fails.
- **Entities** are compared by identity/ID; **value objects** by all their fields.

## Interview questions
1. `==` vs `equals` in Java? Why does `Integer.valueOf(127) == Integer.valueOf(127)` hold but not for 128? *The `Integer` cache covers −128..127.*
2. Why must `hashCode` be consistent with `equals`? What happens if you override only `equals`? *Equal objects land in different buckets, so `contains` fails.*
5. Why should `equals` use `getClass()` or a `final` class rather than `instanceof` in an extensible hierarchy? *Symmetry breaks across subclasses.*
3. Shallow vs deep copy?
4. In C++, what does the defaulted `operator<=>` give you?

## Exercises
1. Make `Customer` hashable by `id` only.
2. Show the bug when a constructor stores a caller's mutable `List` directly, then fix it.

<details><summary>Solutions</summary>

```java
@Override public boolean equals(Object o) { return o instanceof Customer c && c.id == id; }
@Override public int hashCode() { return Long.hashCode(id); }

final class Cart {
    private final List<String> items;
    Cart(List<String> items) { this.items = new ArrayList<>(items); }   // defensive copy, not this.items = items
}
```
</details>
