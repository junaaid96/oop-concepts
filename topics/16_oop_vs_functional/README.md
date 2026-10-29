# 16 · OOP vs Functional Programming — Side by Side

> Full guide with decision flowchart & cheat sheet: **[docs/OOP_VS_FUNCTIONAL.md](../../docs/OOP_VS_FUNCTIONAL.md)**
>
> Files: [`oop_vs_fp.py`](oop_vs_fp.py) · [`oop_vs_fp.cpp`](oop_vs_fp.cpp) · tests: [`test_oop_vs_fp.py`](test_oop_vs_fp.py)

Each problem below is solved **both ways** in the same file so you can compare:

| # | Problem | OOP version | FP version | Usually better |
|---|---|---|---|---|
| 1 | Shopping cart total with discounts | `Cart` object with methods | pipeline of pure functions | FP for the math, OOP for the cart's lifecycle |
| 2 | Shape areas (add new shapes often) | class hierarchy + `area()` | data + `match` function | OOP when adding *types*, FP when adding *operations* |
| 3 | Order processing that can fail | exceptions + mutable state | `Result`/`Optional` chaining, no mutation | FP for validation chains |
| 4 | Bank account (stateful entity) | object guarding invariants | fold over an event list | OOP for live state; FP (event sourcing) for audit/replay |
| 5 | Pricing strategy | Strategy pattern classes | pass a function | FP — fewer moving parts |
| 6 | Parallel word count | shared mutable counter + lock | map/reduce over immutable chunks | FP — no locks needed |

## Interview questions
1. What is a pure function? Why is it easy to test?
2. What is referential transparency?
3. What is the "expression problem"?
4. Explain map, filter, reduce and a higher-order function.
5. How do you manage side effects in a functional design? *Push them to the edges: "functional core, imperative shell".*
6. Is `Optional`/`Maybe` a monad? What do `map` and `flat_map` do?
7. When would you choose OOP over FP for a new project?

## Exercises
1. Rewrite `Cart.total` as a single `reduce`.
2. Add a `Triangle` to both shape versions — count the files/functions you touched in each.
3. Make `withdraw_events` reject an overdraft by returning an error instead of raising.

<details><summary>Solutions</summary>

```python
from functools import reduce
total = reduce(lambda acc, i: acc + i.price * i.qty, items, 0.0)

# OOP: one new class.  FP: one new dataclass + one new case in EVERY function that matches on Shape.

def withdraw_events(events, amount):
    bal = balance_from_events(events)
    return Err("insufficient funds") if amount > bal else Ok(events + (("withdraw", amount),))
```
</details>
