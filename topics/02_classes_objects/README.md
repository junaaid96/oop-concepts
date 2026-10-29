# 02 · Classes, Objects, Constructors, Static, Final & Immutability

> Files: [`Classes.java`](src/main/java/oop/classes/Classes.java) · [`classes.cpp`](classes.cpp) · tests: [`ClassesTest.java`](src/test/java/oop/classes/ClassesTest.java)

- **Class** = blueprint (fields + methods). **Object** = an instance with its own state and identity.
- **Constructor** puts a new object into a *valid* state. In C++ prefer member-initializer lists; mark
  single-argument constructors `explicit`. In Java `new` allocates and the constructor initialises; since
  Java 25 (JEP 513) a constructor may validate arguments *before* calling `super(...)` (see `AdminUser`).
- **Alternative constructors**: static factory methods (`User.fromCsv("ann,ann@x.io")`, `List.of(...)`) in both languages.
- **Static members** belong to the class, not to an instance (counters, factories, constants).
  Java: `static` fields/methods/nested classes, `static final` constants. C++: `static` members / `inline static`.
- **final / const**: C++ `const` members & methods, `final` classes/methods; Java `final` fields (assigned once),
  `final` methods (no override), `final` classes (no subclass). Java has no `const` methods.
- **Immutability**: an object whose state cannot change after construction. Benefits: thread-safe,
  safe to share and cache, usable as `HashMap`/`HashSet` keys, easier reasoning. Java: `record` (shallowly immutable),
  `final` fields + defensive copies (`List.copyOf`); C++: `const` members, return new objects from "wither" methods.

## Rule of zero / three / five (C++)
If a class manages a resource it needs a destructor, copy ctor/assign, move ctor/assign (five).
Best: let members like `std::string`, `std::vector`, `std::unique_ptr` manage resources so you need **zero**.
Java has no destructors or copy constructors: the GC frees memory, `AutoCloseable` frees other resources,
and copying is explicit (copy constructors/factories; avoid `clone()`).

## Interview questions
1. Static factory method vs constructor? *Factories have names, can cache or return subtypes, and can validate before creating; constructors always create a new instance of exactly that class.*
2. Why make a constructor `explicit` in C++?
3. What are the benefits of immutable objects? Any costs? *More allocations/copies.*
4. Is a `record` deeply immutable? *No — a `List` component can still be mutated; copy it with `List.copyOf` in the compact constructor.*
6. What does `final` mean on a field, a method and a class?
5. What is the rule of five?

## Exercises
1. Add `Money.times(double factor)` returning a new `Money`.
2. Give `Temperature` a `fromFahrenheit` static factory.
3. Count how many `Counter` objects were created using a `static` field.

<details><summary>Solutions</summary>

```java
// 1 (inside record Money)
Money times(double factor) { return new Money(Math.round(amount * factor * 100) / 100.0, currency); }

// 2 (inside Temperature)
static Temperature fromFahrenheit(double f) { return new Temperature((f - 32) * 5 / 9); }

// 3
final class Counter {
    private static final AtomicInteger CREATED = new AtomicInteger();
    Counter() { CREATED.incrementAndGet(); }
    static int created() { return CREATED.get(); }
}
```
</details>
