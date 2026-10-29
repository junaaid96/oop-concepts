# 06 · Enums, Records (Data Classes) & Sealed Types

> Files: [`Modeling.java`](src/main/java/oop/modeling/Modeling.java) · [`modeling.cpp`](modeling.cpp) · tests: [`ModelingTest.java`](src/test/java/oop/modeling/ModelingTest.java)

- **Enum**: a closed set of named constants, optionally with data and behaviour.
  Java `enum` is a full class (fields, constructors, methods, `EnumSet`/`EnumMap`); C++ `enum class` (scoped, no implicit int conversion).
- **Record / data class**: a class that is "just data" with generated equality, hashing and repr.
  Java `record Point(int x, int y)` (Java 16+) with compact constructors for validation;
  C++ aggregate `struct` with `= default` comparisons.
- **Sealed hierarchy / sum type**: a *closed* set of variants, so the compiler/type checker can verify
  every case is handled. Java: `sealed interface Shape permits Circle, Square` + an exhaustive
  pattern-matching `switch` (no `default` needed; record patterns, guards with `when`, unnamed `_` in Java 22+);
  C++: `std::variant` + `std::visit`.

This is where OOP meets **algebraic data types** from functional programming: records are
*product types*, sealed hierarchies are *sum types*.

## Interview questions
1. Why is `enum class` safer than a plain C enum?
2. Open class hierarchy (virtual methods) vs closed sum type (variant/match): trade-offs? *Open: easy to add types, hard to add operations. Closed: easy to add operations, hard to add types (the "expression problem").*
3. What does a `record` generate for you? *Private final fields, canonical constructor, accessors, `equals`, `hashCode`, `toString`.*
4. Why does an exhaustive `switch` over a sealed type need no `default` — and why is that a feature?

## Exercises
1. Add a `Triangle` variant to `Shape` and update `area`. What warns you if you forget?
2. Give `OrderStatus` a method `canCancel()`.

<details><summary>Solutions</summary>

```java
sealed interface Shape permits Circle, Square, Triangle {}
record Triangle(double base, double height) implements Shape {}
// in area(): case Triangle(var b, var h) -> 0.5 * b * h;
// The compiler rejects the switch until the new case is handled (no default branch).

boolean canCancel() { return this == PENDING || this == PAID; }
```
</details>
