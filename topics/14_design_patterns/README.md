# 14 · Design Patterns Commonly Used with OOP

> Files: [`patterns.py`](patterns.py) · [`patterns.cpp`](patterns.cpp) · tests: [`test_patterns.py`](test_patterns.py)

| Category | Pattern | Problem it solves | In this folder |
|---|---|---|---|
| Creational | **Factory Method / Simple Factory** | create objects without hard-coding classes | `make_notifier` |
| Creational | **Builder** | build complex objects step by step | `PizzaBuilder` |
| Creational | **Singleton** | one shared instance — *use sparingly*, prefer DI | `Config` (module-level in Python) |
| Structural | **Adapter** | make an incompatible interface fit | `LegacyPrinterAdapter` |
| Structural | **Decorator** | add behaviour by wrapping | `LoggingNotifier` |
| Behavioral | **Strategy** | swap algorithms at runtime | `ShippingStrategy` |
| Behavioral | **Observer** | notify subscribers of events | `EventBus` |
| Behavioral | **Command** | turn requests into objects (undo, queues) | `AddText` |
| Behavioral | **Template Method** | fixed algorithm, variable steps | see [03](../03_interfaces_vs_abstract) |
| Behavioral | **State** | behaviour changes with internal state | see exercises |

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

```python
class ExpressShipping:
    def cost(self, kg): return 15 + 3 * kg

class Red:    def next(self): return Green()
class Green:  def next(self): return Yellow()
class Yellow: def next(self): return Red()
class TrafficLight:
    def __init__(self): self.state = Red()
    def tick(self): self.state = self.state.next()
```
</details>
