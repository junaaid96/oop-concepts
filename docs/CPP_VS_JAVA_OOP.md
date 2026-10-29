# C++ vs Java — the Same OOP Ideas, Two Very Different Machines

> Runnable code for both languages lives in every [`topics/NN_*`](../topics) folder: a `.cpp` file and a
> Java module with JUnit tests. This guide explains *why* the two versions look different.

C++ and Java share syntax and vocabulary (classes, `public`/`private`, inheritance, `virtual`/`@Override`,
templates/generics, exceptions), but they make opposite default choices:

| Default | C++ | Java |
|---|---|---|
| Objects live | on the stack, inside other objects, or on the heap | always on the heap (the JIT may optimise allocation away) |
| Variables hold | **values** (objects themselves) | **references** to objects (plus primitives by value) |
| Method dispatch | static unless `virtual` | virtual unless `static`, `private` or `final` |
| Cleanup | deterministic destructors (RAII) | garbage collector + `AutoCloseable` |
| Multiple inheritance | of classes | of interfaces only (types + default methods) |
| Generics | templates, instantiated per type | type-checked, then **erased** |
| Errors | unchecked exceptions, `noexcept`, `std::expected` | checked + unchecked exceptions |

---

## 1. `virtual`, `override`, `final`

```cpp
struct Shape {
    virtual ~Shape() = default;
    virtual double area() const = 0;          // pure virtual → Shape is abstract
    std::string name() const { return "shape"; }   // NOT virtual: statically bound
};
struct Circle final : Shape {                 // final class: no further subclasses
    double r;
    explicit Circle(double r) : r(r) {}
    double area() const override { return 3.14159 * r * r; }   // override: compiler checks the signature
};
```

```java
abstract class Shape {
    abstract double area();                   // abstract → must be implemented
    String name() { return "shape"; }         // virtual by default
    final String describe() { return name() + " " + area(); }  // final method: cannot be overridden
}
final class Circle extends Shape {            // final class
    private final double r;
    Circle(double r) { this.r = r; }
    @Override double area() { return Math.PI * r * r; }        // @Override: compiler checks it
}
```

- In C++ a method called through a base pointer/reference is dispatched dynamically **only if it is
  `virtual`**. Forgetting `virtual` silently calls the base version. In Java, *every* instance method is
  overridable unless marked `final`/`private`/`static`.
- `override` (C++) and `@Override` (Java) are both optional but should always be used: they turn a
  typo in the signature into a compile error instead of a new, unrelated method.
- Both languages have `final` classes and methods. Java also has `sealed ... permits` to allow a
  *specific* set of subclasses (topic 06); C++ has no direct equivalent (use `std::variant` for closed sets).
- **Calling virtual methods from constructors**: C++ dispatches to the class *currently being built*
  (the base version); Java dispatches to the most-derived override even though its fields are not
  initialised yet — a classic Java bug. Don't call overridable methods from constructors in either language.
- **Overloading vs overriding**: both pick overloads at compile time from the *static* type. C++ also has
  *name hiding*: an overload in a derived class hides all base overloads with the same name unless you add
  `using Base::f;`. Java does not hide overloads this way.

## 2. Virtual destructors (and why Java has none)

```cpp
struct Base { ~Base() { /* ... */ } };            // NOT virtual
struct Derived : Base { std::vector<int> data; };
std::unique_ptr<Base> p = std::make_unique<Derived>();
// p goes out of scope → only ~Base runs → undefined behaviour, Derived::data leaks
```

Rule: **a class meant to be deleted through a base pointer needs a `virtual` destructor** (or a
protected non-virtual one to forbid that use). `std::shared_ptr` happens to remember the right
deleter, but don't rely on it.

Java has no destructors. Memory is reclaimed by the GC, and other resources (files, sockets, locks)
are released by `close()` from `AutoCloseable`, normally via try-with-resources:

```java
try (var conn = new Connection("db")) {   // close() runs even if an exception is thrown
    conn.query("select 1");
}
```

`finalize()` is deprecated for removal; `java.lang.ref.Cleaner` is only a safety net (topic 08).

## 3. Object slicing

```cpp
struct Animal { virtual std::string sound() const { return "..."; } virtual ~Animal() = default; };
struct Dog : Animal { std::string sound() const override { return "Woof"; } };

void byValue(Animal a)            { std::cout << a.sound(); }  // copies only the Animal part → "..."
void byRef(const Animal& a)       { std::cout << a.sound(); }  // → "Woof"
std::vector<Animal> zoo{Dog{}};                                 // sliced!
std::vector<std::unique_ptr<Animal>> ok; ok.push_back(std::make_unique<Dog>());
```

Copying a derived object into a base **by value** keeps only the base sub-object. Fixes: pass by
reference or pointer, store `unique_ptr<Base>` in containers, and make polymorphic bases
non-copyable (`Animal(const Animal&) = delete;`) or give them a virtual `clone()`.

Java **cannot slice**: variables of class type are references, so `Animal a = new Dog();` and
`List<Animal>` always refer to the whole `Dog`.

## 4. Multiple inheritance

C++ allows multiple inheritance of classes, including state:

```cpp
struct Device { int id = 0; };
struct Printer : virtual Device { void print(); };
struct Scanner : virtual Device { void scan(); };
struct Copier  : Printer, Scanner {};     // without `virtual` above, Copier would hold TWO Device::id
```

The *diamond problem* (two copies of a shared base, ambiguous calls) is solved with **virtual
inheritance**, at the cost of complexity and slower base access.

Java allows only single class inheritance but **multiple interface inheritance**. Since Java 8
interfaces can carry behaviour (`default` methods) but never instance state, so there is no state
diamond. A *behaviour* diamond must be resolved explicitly (topic 03):

```java
interface Walker  { default String move() { return "walk"; } }
interface Swimmer { default String move() { return "swim"; } }
final class Duck implements Walker, Swimmer {
    @Override public String move() { return Walker.super.move() + "+" + Swimmer.super.move(); }
}
```

Resolution rules: a class method beats any default; a more specific interface beats its parent;
otherwise the class must override.

## 5. Copy, move and RAII

| | C++ | Java |
|---|---|---|
| `a = b` for objects | copies the value (calls copy assignment) | copies the **reference**; both names share one object |
| Copy | copy ctor / copy assignment (generated or `= delete`) | explicit: copy constructor/factory, `List.copyOf`; avoid `clone()` |
| Move | move ctor / move assignment, `std::move`, `noexcept` | not needed — passing a reference is already cheap |
| Rule of 0/3/5 | if you manage a resource, define (or delete) dtor + copy + move | no equivalent; immutability removes the need to copy |
| Resource release | destructor at end of scope (**RAII**) | `close()` via try-with-resources |
| Ownership | `unique_ptr` (sole), `shared_ptr` (shared), `weak_ptr` (observer) | the GC tracks reachability; `WeakReference` for caches |

RAII in C++ makes cleanup *automatic and exception-safe for every resource*, without `finally`.
Java gets the same guarantee only for objects you open in a try-with-resources header, so forgetting
it leaks the resource (not the memory). Java's answer to "defensive copies" is immutability: a `record`
with `List.copyOf` fields can be shared freely with no copying at all.

## 6. Memory model

| | C++ | Java |
|---|---|---|
| Allocation | stack (automatic), static, heap (`new`/`make_unique`) | heap for all objects; locals and primitives on the stack |
| Deallocation | deterministic: end of scope, `delete`, smart pointers | tracing GC (G1, ZGC, ...), non-deterministic timing |
| Cycles | `shared_ptr` cycles leak → break with `weak_ptr` | collected automatically |
| Dangling pointers / use-after-free | possible (undefined behaviour) | impossible; `null` gives `NullPointerException` |
| Object layout | you control it (members inline, no header) | each object has a header; fields of reference type are pointers |
| Concurrency model | `std::atomic`, `std::memory_order`, data race = UB | Java Memory Model: `volatile`, `synchronized`, `final` field safety, happens-before |
| Equality | `operator==` (can be `= default`) | `==` is identity, `equals`/`hashCode` is value equality |

Consequences for design: in C++ you choose value vs reference semantics *per use*; in Java you
choose it *per type* (primitives and records behave like values, everything else is shared). Java's
`final` fields are guaranteed visible to other threads after construction, which makes immutable
objects safe to publish without locks.

## 7. Templates vs generics

```cpp
template <typename T>
concept Shape = requires(const T& s) { { s.area() } -> std::convertible_to<double>; };

template <Shape S>
double total(const std::vector<S>& shapes) { double t = 0; for (auto& s : shapes) t += s.area(); return t; }
```

```java
static double total(List<? extends Shape> shapes) {      // bounded wildcard
    return shapes.stream().mapToDouble(Shape::area).sum();
}
static <T extends Comparable<? super T>> T smallest(List<T> xs) { return Collections.min(xs); }
```

| | C++ templates | Java generics |
|---|---|---|
| Implementation | compile-time code generation per type (monomorphisation) | one compiled version; type arguments **erased** to bounds |
| Type arguments | any type, including `int` and values (`std::array<int, 3>`) | reference types only (`List<Integer>`, boxing) |
| Constraints | C++20 `concept`s / `requires` (structural) | `extends` bounds (nominal), `&` for multiple bounds |
| Runtime type info | full (`T` is a real type in each instantiation) | none: no `new T()`, no `instanceof List<String>` |
| Variance | none built in; each instantiation is unrelated | invariant, with use-site wildcards (PECS: `? extends`, `? super`) |
| Specialisation | full and partial specialisation, metaprogramming | not available |
| Cost | zero runtime overhead, larger binaries, slower builds | no code bloat; casts inserted by the compiler, boxing for primitives |
| Errors | historically long; concepts make them readable | checked at declaration, clear messages |

Templates are a compile-time *code generator* (and a Turing-complete metaprogramming language);
Java generics are a compile-time *type checker* layered over `Object`.

## 8. Quick translation table

| C++ | Java |
|---|---|
| `class D : public B` | `class D extends B` |
| pure virtual `= 0` | `abstract` method |
| class with only pure virtuals | `interface` |
| `override` / `final` | `@Override` / `final` |
| `std::variant` + `std::visit` | `sealed interface` + records + `switch` |
| aggregate `struct` with `operator== = default` | `record` |
| `enum class` | `enum` (with fields and methods) |
| `const` member function | no equivalent — use immutable types |
| `friend` | package-private access |
| namespace | package (and modules) |
| `std::function`, lambda | functional interface, lambda |
| `std::optional` / `std::expected` | `Optional` / a sealed `Result` |
| `unique_ptr` + destructor | reference + `AutoCloseable` |
| `static_cast` / `dynamic_cast` | cast / `instanceof` pattern (`if (s instanceof Circle c)`) |

## Interview questions
1. Why are Java methods virtual by default while C++ methods are not? *C++ follows "you don't pay for what you don't use" (no vtable lookup unless asked); Java favours extensibility and lets the JIT devirtualise.*
2. Why can't Java objects be sliced?
3. When does a C++ class need a virtual destructor? Why doesn't Java need one?
4. How does Java avoid the diamond problem that C++ solves with virtual inheritance?
5. What does a C++ move constructor do, and why has Java no equivalent?
6. Explain type erasure. Name two things C++ templates can do that Java generics cannot.
7. What happens when a constructor calls an overridable method in C++ vs Java?
8. How are reference cycles handled by `shared_ptr` vs the Java GC?

## Further reading
- Bjarne Stroustrup, *A Tour of C++* (3rd ed.) and the [C++ Core Guidelines](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines).
- Joshua Bloch, *Effective Java* (3rd ed.).
- [Java Language Specification, Java SE 25](https://docs.oracle.com/javase/specs/jls/se25/html/index.html).
- [cppreference.com](https://en.cppreference.com/).
