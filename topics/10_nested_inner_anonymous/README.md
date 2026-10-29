# 10 · Nested, Inner, Local & Anonymous Classes (and Lambdas)

> Files: [`Nested.java`](src/main/java/oop/nested/Nested.java) · [`nested.cpp`](nested.cpp) · tests: [`NestedTest.java`](src/test/java/oop/nested/NestedTest.java)

| Kind | Meaning | C++ | Java |
|---|---|---|---|
| Nested (static) class | a class scoped inside another, no link to an outer instance | `class Outer { class Node {}; };` | `static class Node` |
| Inner class | holds a reference to an enclosing *instance* (Java non-static inner class) | pass the outer pointer explicitly | non-`static` member class; `outer.new Inner()`, `Outer.this.x` |
| Local class | defined inside a function | supported | class declared in a method; captures *effectively final* locals |
| Anonymous class | one-off implementation of an interface | lambda / unnamed struct | `new Greeter() { ... }` or a lambda for functional interfaces |

Use them to **hide helper types** (iterators, nodes, builders) and keep the public API small.
Lambdas/closures replaced most anonymous classes once languages added first-class functions.

## Interview questions
1. Nested static class vs inner class in Java — and how do you emulate each in C++?
4. Why can lambdas only capture effectively final locals? What does `this` mean in a lambda vs an anonymous class?
5. Why can an inner class cause a memory leak? *It keeps its outer instance reachable.*
2. What is a closure? What does it capture in C++ (`[=]`, `[&]`, `[this]`)?
3. Why is a nested `Node` class a good encapsulation choice for a linked list?

## Exercises
1. Add `size()` to `LinkedList` using its nested `Node`.
2. Write `makeCounter()` returning an `IntSupplier` that increments and returns a count.

<details><summary>Solutions</summary>

```java
int size() { int n = 0; for (Node<T> n0 = head; n0 != null; n0 = n0.next) n++; return n; }

static IntSupplier makeCounter() {
    var n = new int[1];                 // locals must be effectively final, so box the state
    return () -> ++n[0];
}
```
</details>
