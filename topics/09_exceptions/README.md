# 09 · Exceptions in OOP

> Files: [`errors.py`](errors.py) · [`errors.cpp`](errors.cpp) · tests: [`test_errors.py`](test_errors.py)

- Build a **domain exception hierarchy** (`AppError → PaymentError → InsufficientFunds`) so callers can
  catch at the right granularity.
- Throw early (validate in constructors), catch late (at a boundary that can act: UI, API handler, job runner).
- **Chain** causes (`raise X from e` / `std::throw_with_nested`) — never lose the original error.
- **Exception safety** (C++): *basic* (no leaks, valid state), *strong* (commit-or-rollback), *nothrow*.
  Copy-and-swap gives the strong guarantee. Mark move operations and destructors `noexcept`.
- An overriding method should not throw broader exceptions than the base promises (LSP).
- Don't use exceptions for normal control flow; for *expected* failures consider result types
  (`Optional`, `std::expected` in C++23, a `Result` class) — see the FP guide.

## Interview questions
1. Checked vs unchecked exceptions? (Java has both; C++/Python only unchecked.)
2. What are the exception-safety guarantees?
3. Why shouldn't destructors throw?
4. What does `raise ... from ...` do?
5. When is returning an error value better than throwing?

## Exercises
1. Add `AccountFrozen(PaymentError)` and raise it from `Wallet.pay` when frozen.
2. Make `transfer` give the strong guarantee.

<details><summary>Solutions</summary>

```python
class AccountFrozen(PaymentError): pass
# in pay(): if self.frozen: raise AccountFrozen(self.owner)
# transfer: validate everything first, then mutate both wallets (see errors.py)
```
</details>
