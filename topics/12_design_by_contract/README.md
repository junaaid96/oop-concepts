# 12 · Design by Contract

> Files: [`Dbc.java`](src/main/java/oop/dbc/Dbc.java) · [`dbc.cpp`](dbc.cpp) · tests: [`DbcTest.java`](src/test/java/oop/dbc/DbcTest.java)

Bertrand Meyer (Eiffel) described every method as a **contract**:
- **Preconditions** — what the caller must guarantee (`amount > 0`).
- **Postconditions** — what the method guarantees afterwards (`balance == old + amount`).
- **Class invariants** — always true between public calls (`balance >= 0`).

Subtyping rule (ties to LSP): a subclass may **weaken preconditions** and **strengthen
postconditions**, never the reverse.

Implementation options: explicit checks that throw (`Objects.requireNonNull`, `Objects.checkIndex`,
`IllegalArgumentException`), `assert` (disabled unless the JVM runs with `-ea`; Maven Surefire enables it
for tests; C++ disables it with `NDEBUG`), C++26 contracts (`pre`/`post`/`contract_assert`), or
property-based tests (jqwik) to probe contracts.

## Interview questions
1. Precondition vs postcondition vs invariant?
2. Should you use `assert` to validate user input? *No — asserts can be disabled; validate input with exceptions.*
3. How does DbC relate to Liskov substitution?
4. Why are Java `assert` statements off by default, and how do you turn them on? *`java -ea`.*

## Exercises
1. Add a `require` precondition that a `withdraw` amount is ≤ balance.
2. Write an invariant for a `SortedList` and check it after `insert`.

<details><summary>Solutions</summary>

```java
void withdraw(double amount) {
    require(amount > 0 && amount <= balance, "amount within balance");
    balance -= amount;
    checkInvariant();
}

private void checkInvariant() {        // SortedList<Integer>
    for (int i = 1; i < items.size(); i++)
        assert items.get(i - 1) <= items.get(i) : "items sorted";
}
```
</details>
