# 03 · Interfaces vs Abstract Classes

> Files: [`Contracts.java`](src/main/java/oop/contracts/Contracts.java) · [`contracts.cpp`](contracts.cpp) · tests: [`ContractsTest.java`](src/test/java/oop/contracts/ContractsTest.java)

| | Interface | Abstract class |
|---|---|---|
| Purpose | A **capability / contract** ("can be saved", "can fly") | A **partial implementation** of a family ("is a vehicle") |
| State | no instance fields (only `static final` constants) | can hold fields |
| Implementation | `default`, `static` and `private` methods (Java 8/9+) | some concrete methods + some abstract ones |
| How many | a class may implement many | usually single base |
| Java | `interface` (may be `sealed`, may be a `@FunctionalInterface`) | `abstract class` with constructors, fields, `final` template methods |
| C++ | class with only pure virtual functions (+ virtual dtor), or a C++20 `concept` | class with some pure virtual and some implemented members |

**Rule of thumb:** depend on small interfaces; use an abstract class to share code between
closely related implementations (e.g. a *template method*).

### Java interface method kinds (see `Greeter`)
- `abstract` — the contract every implementation must provide.
- `default` — inherited behaviour implementations may override (lets you evolve an interface).
- `static` — helpers/factories on the interface itself (`Greeter.of(...)`), not inherited.
- `private` — shared code for default methods, hidden from implementers.

**Diamond rule**: if a class inherits the same default method from two interfaces (`Walker`, `Swimmer`)
it must override it and can pick one with `Walker.super.move()`. Class methods always win over defaults.

## Interview questions
1. When would you pick an abstract class over an interface?
2. What are `default`, `static` and `private` interface methods for? How is the default-method diamond resolved?
3. Can an abstract class have a constructor? *Yes — to initialise shared state; it runs from subclass constructors.*
4. What is the Template Method pattern?
5. What is the Interface Segregation Principle?

## Exercises
1. Add a `CsvExporter` implementing `Exporter`.
2. Add a `Truck` subclass of `Vehicle` with 6 wheels.

<details><summary>Solutions</summary>

```java
final class CsvExporter implements Exporter {
    @Override public String export(List<List<Object>> rows) {
        return rows.stream()
                .map(r -> r.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .collect(Collectors.joining("\n"));
    }
}

final class Truck extends Vehicle {
    Truck(String name) { super(name); }
    @Override public int wheels() { return 6; }
}
```
</details>
