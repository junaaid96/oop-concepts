# 13 · Dependency Injection

> Files: [`di.py`](di.py) · [`di.cpp`](di.cpp) · tests: [`test_di.py`](test_di.py)

**Dependency injection**: an object receives its collaborators from the outside instead of creating
them. It is the practical tool behind the **Dependency Inversion Principle**.

- **Constructor injection** (preferred): required dependencies, object is complete once built.
- **Setter/property injection**: optional or changeable dependencies.
- **Parameter (method) injection**: dependency needed for one call only.
- **Composition root**: the one place (e.g. `main`) where concrete classes are wired together.
- **DI containers** (Spring, .NET, `dependency-injector` in Python) automate wiring — useful in big apps,
  overkill in small ones. Avoid the **Service Locator** anti-pattern (hidden dependencies).

Benefits: easy testing with fakes, swappable implementations, explicit dependencies.

## Interview questions
1. DI vs DIP vs IoC?
2. Why prefer constructor injection?
3. What is wrong with a Service Locator?
4. How does DI make unit tests faster and more reliable?

## Exercises
1. Inject a `Clock` into `GreetingService` so "Good morning/evening" is testable.
2. Wire a `ConsoleLogger` in the composition root.

<details><summary>Solutions</summary>

```python
class GreetingService:
    def __init__(self, clock): self.clock = clock
    def greet(self): return "Good morning" if self.clock.now().hour < 12 else "Good evening"
class FixedClock:
    def __init__(self, dt): self.dt = dt
    def now(self): return self.dt
```
</details>
