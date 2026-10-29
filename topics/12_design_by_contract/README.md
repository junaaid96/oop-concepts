# 12 · Design by Contract

> Files: [`contracts_dbc.py`](contracts_dbc.py) · [`dbc.cpp`](dbc.cpp) · tests: [`test_dbc.py`](test_dbc.py)

Bertrand Meyer (Eiffel) described every method as a **contract**:
- **Preconditions** — what the caller must guarantee (`amount > 0`).
- **Postconditions** — what the method guarantees afterwards (`balance == old + amount`).
- **Class invariants** — always true between public calls (`balance >= 0`).

Subtyping rule (ties to LSP): a subclass may **weaken preconditions** and **strengthen
postconditions**, never the reverse.

Implementation options: explicit checks that raise, `assert` (disabled with `python -O` /
`NDEBUG`), decorators, type hints, C++26 contracts (`pre`/`post`/`contract_assert`), or
property-based tests (Hypothesis) to probe contracts.

## Interview questions
1. Precondition vs postcondition vs invariant?
2. Should you use `assert` to validate user input? *No — asserts can be disabled; validate input with exceptions.*
3. How does DbC relate to Liskov substitution?

## Exercises
1. Add a `@requires` decorator check that `transfer` amount ≤ balance.
2. Write an invariant for a `SortedList` and check it after `insert`.

<details><summary>Solutions</summary>

```python
@requires(lambda self, amount: 0 < amount <= self.balance, "amount within balance")
def withdraw(self, amount): ...

def _invariant(self): assert all(a <= b for a, b in zip(self.items, self.items[1:]))
```
</details>
