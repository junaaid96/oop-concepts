# 14 · Design Patterns Commonly Used with OOP

> Files: [`Patterns.java`](src/main/java/oop/patterns/Patterns.java) · [`patterns.cpp`](patterns.cpp) · tests: [`PatternsTest.java`](src/test/java/oop/patterns/PatternsTest.java)

| Category | Pattern | Problem it solves | In this folder |
|---|---|---|---|
| Creational | **Factory Method / Simple Factory** | create objects without hard-coding classes | `Notification.create` (sealed) |
| Creational | **Builder** | build complex objects step by step | `Pizza.builder()` |
| Creational | **Singleton** | one shared instance — *use sparingly*, prefer DI | `Config` (enum singleton) |
| Structural | **Adapter** | make an incompatible interface fit | `ThermometerAdapter` |
| Structural | **Decorator** | add behaviour by wrapping | `Coffee` / `WithMilk` |
| Behavioral | **Strategy** | swap algorithms at runtime | `ShippingStrategy` |
| Behavioral | **Observer** | notify subscribers of events | `EventBus` |
| Behavioral | **Command** | turn requests into objects (undo, queues) | `TextEditor` + `Command` |
| Behavioral | **Template Method** | fixed algorithm, variable steps | see [03](../03_interfaces_vs_abstract) |
| Behavioral | **State** | behaviour changes with internal state | `Light` (enum states) |

Many patterns shrink to a function when the language has first-class functions (Strategy → pass a
lambda, Command → a closure, Observer → a list of callbacks). See [`docs/OOP_VS_FUNCTIONAL.md`](../../docs/OOP_VS_FUNCTIONAL.md).

Reference: [refactoring.guru/design-patterns](https://refactoring.guru/design-patterns), *Design Patterns* (GoF, 1994).

## Interview questions
1. Strategy vs State? *Same structure; State objects switch themselves, Strategy is chosen by the client.*
2. Decorator vs inheritance for adding behaviour?
3. Why is Singleton often considered an anti-pattern? *Global state, hidden dependencies, hard to test.*
4. Adapter vs Facade?
5. How would you implement undo/redo?

## Exercises
1. Add an `ExpressShipping` strategy.
2. Implement a traffic light with the State pattern (`Red → Green → Yellow → Red`).

<details><summary>Solutions</summary>

```java
final class ExpressShipping implements ShippingStrategy {
    @Override public double cost(double kg) { return 15 + 3 * kg; }
}
// or simply: ShippingStrategy express = kg -> 15 + 3 * kg;

enum TrafficLight {
    RED { TrafficLight next() { return GREEN; } },
    GREEN { TrafficLight next() { return YELLOW; } },
    YELLOW { TrafficLight next() { return RED; } };
    abstract TrafficLight next();
}
```
</details>
