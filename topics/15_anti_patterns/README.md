# 15 · OOP Anti-Patterns (and their fixes)

> Files: [`AntiPatterns.java`](src/main/java/oop/antipatterns/AntiPatterns.java) · [`anti_patterns.cpp`](anti_patterns.cpp) · tests: [`AntiPatternsTest.java`](src/test/java/oop/antipatterns/AntiPatternsTest.java)

| Anti-pattern | Symptom | Fix |
|---|---|---|
| **God object** | one class knows/does everything (`AppManager` with 80 methods) | split by responsibility (SRP), extract collaborators |
| **Anemic domain model** | data-only classes + "service" classes holding all logic; invariants enforced nowhere | move behaviour next to the data it protects |
| **Deep inheritance** | `A → B → C → D → E`; changes ripple; hard to know which method runs | flatten; compose behaviours; prefer interfaces |
| **Getter/setter everything** | encapsulation in name only | "tell, don't ask" — expose operations |
| **Primitive obsession** | `String` emails, `double` money everywhere | small value objects (`Email`, `Money`) |
| **Refused bequest** | subclass overrides methods to throw | wrong hierarchy; LSP violation |
| **Singleton abuse / global state** | hidden coupling, flaky tests | dependency injection |
| **Feature envy** | a method mostly uses another object's data | move the method there |
| **Yo-yo problem / Speculative generality** | abstractions with one implementation "just in case" | YAGNI, inline them |

Note: an anemic model is a *good fit* in FP-style or data-pipeline code where data and functions are
deliberately separate — it's an anti-pattern when you *intended* rich domain objects.

## Interview questions
1. What is a god object and how do you refactor one safely? *Add tests, then extract classes one responsibility at a time.*
2. Anemic vs rich domain model?
3. Why are deep hierarchies fragile?
4. What is "tell, don't ask"?

## Exercises
1. Find the god object in your last project and list three classes you could extract.
2. Replace `order.setStatus("paid")` calls with an `order.pay()` method that enforces rules.

<details><summary>Solutions</summary>

See `RichOrder.pay()` in [`AntiPatterns.java`](src/main/java/oop/antipatterns/AntiPatterns.java) — it validates the state transition the anemic
version leaves to every caller.
</details>
