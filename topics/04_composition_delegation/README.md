# 04 · Composition vs Inheritance, and Delegation

> Files: [`Composition.java`](src/main/java/oop/composition/Composition.java) · [`composition.cpp`](composition.cpp) · tests: [`CompositionTest.java`](src/test/java/oop/composition/CompositionTest.java)

**"Favour composition over inheritance"** (Gang of Four). Composition = an object *has* other
objects and uses them ("has-a"). Inheritance = an object *is* a specialised kind of another ("is-a").

| Use inheritance when | Use composition when |
|---|---|
| true is-a, substitutable (LSP holds) | you want to reuse behaviour only |
| hierarchy is shallow and stable | behaviour must change at runtime |
| framework requires it (e.g. extending `RuntimeException`) | you need many combinations (avoid class explosion) |

**Delegation**: the outer object forwards a request to a helper it owns. It gives you reuse
without inheritance's tight coupling. Java has no automatic forwarding — write the forwarding
methods (or extend a `java.util.AbstractList`-style skeleton, as `LoggingList` does).

**Classic trap**: `class BadStack<T> extends ArrayList<T>` exposes `add(int, T)`, `sort`, `remove(0)`… which break the
stack contract (`java.util.Stack extends Vector` made exactly this mistake). Wrap an `ArrayDeque` instead.
`Collections.unmodifiableList` is another composition example: a read-only *view* that delegates.

## Interview questions
1. Why is inheritance called "white-box reuse" and composition "black-box reuse"?
2. What is the fragile base class problem?
3. Is `Square extends Rectangle` a good idea? *No — setting width independently breaks LSP.*
4. How do you swap behaviour at runtime? *Inject a different strategy object.*

## Exercises
1. Add a `TurboEngine` and give a car one at runtime.
2. Implement a `LoggingList` that delegates to a list but logs every `append`.

<details><summary>Solutions</summary>

```java
final class TurboEngine implements Engine {
    @Override public String start() { return "turbo whoosh"; }
}
var car = new Car(new ElectricEngine());
car.setEngine(new TurboEngine());           // behaviour swapped at runtime

final class LoggingList<T> extends AbstractList<T> {   // delegation to a private list
    private final List<T> items = new ArrayList<>();
    final List<String> log = new ArrayList<>();
    @Override public boolean add(T x) { log.add("add " + x); return items.add(x); }
    @Override public T get(int i) { return items.get(i); }
    @Override public int size() { return items.size(); }
}
```
</details>
