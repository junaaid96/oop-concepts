# 11 · SOLID Overview

> Files: [`Solid.java`](src/main/java/oop/solid/Solid.java) · [`solid.cpp`](solid.cpp) · tests: [`SolidTest.java`](src/test/java/oop/solid/SolidTest.java)

| Letter | Principle | One-liner | Smell when violated |
|---|---|---|---|
| **S** | Single Responsibility | a class has one reason to change | "Manager"/"Utils" classes doing everything |
| **O** | Open/Closed | open for extension, closed for modification | growing `if type == ...` chains |
| **L** | Liskov Substitution | subtypes must be usable wherever the base is | overrides that throw "not supported" |
| **I** | Interface Segregation | many small client-specific interfaces | implementers with empty methods |
| **D** | Dependency Inversion | depend on abstractions, not concretions | `new MySqlDb()` inside business logic |

The examples refactor a small **invoice** feature step by step to satisfy each principle.

SOLID is a set of heuristics, not laws: apply it where change is likely, avoid speculative abstraction (YAGNI).

## Further reading
- Robert C. Martin, *Design Principles and Design Patterns* (2000) and *Clean Architecture*.
- [SOLID on Wikipedia](https://en.wikipedia.org/wiki/SOLID)
- Barbara Liskov & Jeannette Wing, *A Behavioral Notion of Subtyping* (1994).
- [refactoring.guru — design principles & patterns](https://refactoring.guru/design-patterns)

## Interview questions
1. Explain each SOLID principle with an example.
2. Why is `Square` inheriting `Rectangle` an LSP violation?
3. How does DIP differ from dependency injection? *DIP is the principle (depend on abstractions); DI is a technique to supply them.*
4. Can following SOLID make code worse? *Yes — over-abstraction, too many tiny classes.*

## Exercises
1. Add a `PercentDiscount` without editing `InvoiceCalculator` (OCP).
2. Split a fat `Machine` interface (`print`, `scan`, `fax`) for a printer-only device (ISP).

<details><summary>Solutions</summary>

```java
record PercentDiscount(double pct) implements Discount {
    @Override public double apply(double total) { return total * (1 - pct / 100); }
}

interface Printer { String print(String doc); }
interface Scanner { String scan(); }
final class SimplePrinter implements Printer {          // implements only what it supports
    @Override public String print(String doc) { return "printed " + doc; }
}
```
</details>
