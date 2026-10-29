# 01 · The Four Pillars, Deepened

> Files: [`Pillars.java`](src/main/java/oop/pillars/Pillars.java) · [`pillars.cpp`](pillars.cpp) · tests: [`PillarsTest.java`](src/test/java/oop/pillars/PillarsTest.java)

## 1. Encapsulation
Bundle data with the methods that operate on it, and **protect invariants** by hiding state.
It is *not* "make every field private and add getters/setters" — that just moves the problem.
Good encapsulation exposes **behaviour** (`account.withdraw(50)`) rather than raw state (`account.balance -= 50`).

| Language | Mechanism |
|---|---|
| C++ | `private` / `protected` / `public`, `friend` (use sparingly) |
| Java | `private` / package-private / `protected` / `public`, modules (`exports`), records and immutable fields |

## 2. Abstraction
Show *what* an object does, hide *how*. Clients program against a small, stable surface
(an interface / abstract base class) and the details can change freely.

## 3. Inheritance
An "is-a" relationship that reuses and specialises behaviour. Prefer **shallow** hierarchies;
use it when the subclass truly is substitutable for the base (Liskov Substitution Principle).
See [04_composition_delegation](../04_composition_delegation) for the usual alternative.

## 4. Polymorphism — the four kinds
| Kind | Also called | C++ | Java |
|---|---|---|---|
| **Subtype (dynamic)** | runtime / late binding | `virtual` + override | every non-`static`, non-`private`, non-`final` instance method is virtual; `@Override` |
| **Ad hoc (static)** | overloading, operator overloading | function/operator overloading | method overloading (no operator overloading); pattern `switch` over types |
| **Parametric** | generics | templates | generics `class Stack<T>`, `<T extends Comparable<T>>` |
| **Coercion** | implicit conversion | converting constructors | widening `int → long → double`, autoboxing |

Java is **nominally** typed: two classes are interchangeable only if they share a declared interface
(`Speaker` in the example). Lambdas give you a lightweight way to implement one-method interfaces.

## Common mistakes
- Exposing setters for everything (breaks encapsulation).
- Using inheritance just to reuse code (use composition).
- Forgetting a `virtual` destructor in a C++ polymorphic base (UB when deleting via base pointer).
- Confusing overloading (chosen at compile time from the *static* type) with overriding (chosen at run time).
- Calling an overridable method from a constructor — the subclass override runs before the subclass is initialised.

## Interview questions
1. What is the difference between encapsulation and abstraction? *Encapsulation hides state and guards invariants; abstraction hides complexity behind a simple contract.*
2. Name the kinds of polymorphism with an example of each.
3. Why does a polymorphic C++ base class need a virtual destructor?
4. In Java every instance method is virtual by default — how do you opt out? *`final`, `private` or `static` methods are not overridden.*
6. Overloading vs overriding: which is resolved at compile time? *Overloading; overriding is resolved at run time via dynamic dispatch.*
5. What is object slicing in C++ and how do you avoid it? *Copying a derived object into a base by value drops the derived part; pass by reference/pointer.*

## Exercises
1. Add a `Triangle` shape that works with `totalArea` without changing `totalArea`.
2. Make `BankAccount` reject withdrawals that exceed the balance plus an overdraft limit.
3. Write a `describe(Object)` method that handles `Integer`, `String` and `List` differently with a pattern `switch`.

<details><summary>Solutions</summary>

```java
// 1. Open/closed in action: only a new class is needed.
final class Triangle extends Shape {
    private final double base, height;
    Triangle(double base, double height) { this.base = base; this.height = height; }
    @Override public double area() { return 0.5 * base * height; }
}

// 2.
class OverdraftAccount extends BankAccount {
    private final double limit;
    OverdraftAccount(String owner, double limit) { super(owner); this.limit = limit; }
    @Override public void withdraw(double amount) {
        if (amount > balance() + limit) throw new IllegalArgumentException("limit exceeded");
        balance -= amount;                  // `balance` is protected in the base class
    }
}

// 3. Pattern matching for switch (Java 21+)
static String describe(Object x) {
    return switch (x) {
        case Integer i -> "an int";
        case String s  -> "a string";
        case List<?> l -> "a list of " + l.size();
        default        -> "something";
    };
}
```
</details>
