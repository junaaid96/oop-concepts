# 13 · Dependency Injection

> Files: [`Di.java`](src/main/java/oop/di/Di.java) · [`di.cpp`](di.cpp) · tests: [`DiTest.java`](src/test/java/oop/di/DiTest.java)

**Dependency injection**: an object receives its collaborators from the outside instead of creating
them. It is the practical tool behind the **Dependency Inversion Principle**.

- **Constructor injection** (preferred): required dependencies, object is complete once built.
- **Setter/property injection**: optional or changeable dependencies.
- **Parameter (method) injection**: dependency needed for one call only.
- **Composition root**: the one place (e.g. `main`) where concrete classes are wired together.
- **DI containers** (Spring, Guice, Dagger, Jakarta CDI) automate wiring — useful in big apps,
  overkill in small ones. Avoid the **Service Locator** anti-pattern (hidden dependencies).

Benefits: easy testing with fakes, swappable implementations, explicit dependencies.
`java.time.Clock` is the classic injectable dependency; `ScopedValue` (final in Java 25) passes
per-request context without threading a parameter through every call.

## Interview questions
1. DI vs DIP vs IoC?
2. Why prefer constructor injection?
3. What is wrong with a Service Locator?
4. How does DI make unit tests faster and more reliable?

## Exercises
1. Inject a `Clock` into `GreetingService` so "Good morning/evening" is testable.
2. Wire a `ConsoleLogger` in the composition root.

<details><summary>Solutions</summary>

```java
final class GreetingService {
    private final Clock clock;
    GreetingService(Clock clock) { this.clock = clock; }
    String greet() { return LocalTime.now(clock).getHour() < 12 ? "Good morning" : "Good evening"; }
}
// test: new GreetingService(Clock.fixed(Instant.parse("2026-01-01T08:00:00Z"), ZoneOffset.UTC))
```
</details>
