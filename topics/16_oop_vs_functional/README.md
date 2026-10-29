# 16 · OOP vs Functional Programming — Side by Side

> Full guide with decision flowchart & cheat sheet: **[docs/OOP_VS_FUNCTIONAL.md](../../docs/OOP_VS_FUNCTIONAL.md)**
>
> Files: [`OopVsFp.java`](src/main/java/oop/oopvsfp/OopVsFp.java) · [`oop_vs_fp.cpp`](oop_vs_fp.cpp) · tests: [`OopVsFpTest.java`](src/test/java/oop/oopvsfp/OopVsFpTest.java)

Each problem below is solved **both ways** in the same file so you can compare:

| # | Problem | OOP version | FP version | Usually better |
|---|---|---|---|---|
| 1 | Shopping cart total with discounts | `Cart` object with methods | pipeline of pure functions | FP for the math, OOP for the cart's lifecycle |
| 2 | Shape areas (add new shapes often) | class hierarchy + `area()` | sealed records + `switch` | OOP when adding *types*, FP when adding *operations* |
| 3 | Order processing that can fail | exceptions + mutable state | sealed `Result` / `Optional` chaining, no mutation | FP for validation chains |
| 4 | Bank account (stateful entity) | object guarding invariants | fold over an event list | OOP for live state; FP (event sourcing) for audit/replay |
| 5 | Pricing strategy | Strategy pattern classes | pass a function | FP — fewer moving parts |
| 6 | Parallel word count | shared mutable counter + lock | parallel stream `groupingByConcurrent` | FP — no locks needed |

## Interview questions
1. What is a pure function? Why is it easy to test?
2. What is referential transparency?
3. What is the "expression problem"?
4. Explain map, filter, reduce and a higher-order function.
5. How do you manage side effects in a functional design? *Push them to the edges: "functional core, imperative shell".*
6. Is `Optional`/`Maybe` a monad? What do `map` and `flatMap` do?
8. Why are Java streams lazy, and why must stream lambdas be side-effect free?
7. When would you choose OOP over FP for a new project?

## Exercises
1. Rewrite `Cart.total` as a single `reduce`.
2. Add a `Triangle` to both shape versions — count the files/functions you touched in each.
3. Make a `withdrawEvents` function reject an overdraft by returning `Err` instead of throwing.

<details><summary>Solutions</summary>

```java
double total = items.stream().reduce(0.0, (acc, i) -> acc + i.price() * i.qty(), Double::sum);

// OOP: one new class.  FP: one new record in the sealed interface + one new case in EVERY switch over Shape
// (the compiler lists them for you).

static Result<List<Event>> withdrawEvents(List<Event> events, double amount) {
    double bal = balanceFromEvents(events);
    if (amount > bal) return new Err<>("insufficient funds");
    var next = new ArrayList<>(events); next.add(new Withdrew(amount));
    return new Ok<>(List.copyOf(next));
}
```
</details>
