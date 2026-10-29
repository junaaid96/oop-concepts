# 09 · Exceptions in OOP

> Files: [`Errors.java`](src/main/java/oop/errors/Errors.java) · [`errors.cpp`](errors.cpp) · tests: [`ErrorsTest.java`](src/test/java/oop/errors/ErrorsTest.java)

- Build a **domain exception hierarchy** (`AppException → PaymentException → InsufficientFundsException`) so callers can
  catch at the right granularity.
- Throw early (validate in constructors), catch late (at a boundary that can act: UI, API handler, job runner).
- **Chain** causes (`new GatewayException(msg, cause)` / `std::throw_with_nested`) — never lose the original error.
- **Checked vs unchecked** (Java): checked exceptions (`IOException`) are part of the signature; wrap them
  in an unchecked one (`UncheckedIOException`) when callers can't recover. Use multi-catch `catch (A | B e)`
  and try-with-resources (suppressed exceptions keep secondary failures).
- **Exception safety** (C++): *basic* (no leaks, valid state), *strong* (commit-or-rollback), *nothrow*.
  Copy-and-swap gives the strong guarantee. Mark move operations and destructors `noexcept`.
- An overriding method should not throw broader exceptions than the base promises (LSP).
- Don't use exceptions for normal control flow; for *expected* failures consider result types
  (`Optional`, `std::expected` in C++23, a `Result` class) — see the FP guide.

## Interview questions
1. Checked vs unchecked exceptions? When would you wrap a checked one? (Java has both; C++ only unchecked.)
2. What are the exception-safety guarantees?
3. Why shouldn't destructors throw?
4. What is exception chaining (`getCause()`) and what are suppressed exceptions?
5. When is returning an error value better than throwing?

## Exercises
1. Add `AccountFrozenException extends PaymentException` and throw it from `Wallet.pay` when frozen.
2. Make `transfer` give the strong guarantee.

<details><summary>Solutions</summary>

```java
final class AccountFrozenException extends PaymentException {
    AccountFrozenException(String owner) { super(owner + " is frozen"); }
}
// in pay(): if (frozen) throw new AccountFrozenException(owner);
// transfer: validate everything first, then mutate both wallets (see Errors.transfer)
```
</details>
