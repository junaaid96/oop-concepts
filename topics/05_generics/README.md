# 05 · Generics (Parametric Polymorphism)

> Files: [`Generics.java`](src/main/java/oop/generics/Generics.java) · [`generics.cpp`](generics.cpp) · tests: [`GenericsTest.java`](src/test/java/oop/generics/GenericsTest.java)

Write code **once** that works for many types while keeping type safety.

- **Java**: `record Box<T>(T value)`, generic methods `static <T> Optional<T> first(List<T> xs)`,
  bounds `<T extends Comparable<? super T>>`, multiple bounds `<T extends Number & Comparable<T>>`.
  Generics are checked by the compiler and then **erased**: at run time a `List<String>` is just a `List`
  (no `new T()`, no `instanceof List<String>`, no primitive type arguments — use `Integer`).
- **C++**: templates are instantiated at compile time (zero runtime cost). C++20 **concepts**
  constrain template parameters with readable errors.
- **Variance**: `List<Cat>` is not a `List<Animal>` (generics are invariant). Use wildcards at the
  call site — **PECS**: *Producer `extends`, Consumer `super`* (`sum(List<? extends Number>)`,
  `addInts(List<? super Integer>)`). Arrays are covariant and fail at run time (`ArrayStoreException`).

## Interview questions
1. Generics vs `object`/`void*`: what do you gain? *Compile-time type safety, no casts.*
2. Explain covariance and contravariance with an example.
3. C++ templates vs Java generics? *Templates generate code per type (monomorphisation); Java erases types.* See [docs/CPP_VS_JAVA_OOP.md](../../docs/CPP_VS_JAVA_OOP.md).
5. What is type erasure and what can't you do because of it?
6. Explain PECS with `Collections.copy(List<? super T> dest, List<? extends T> src)`.
4. What is a bounded type parameter / concept?

## Exercises
1. Write a generic `maxBy(items, key)` method.
2. Write a C++ concept `Addable` and a generic `sum` function.

<details><summary>Solutions</summary>

```java
static <T, K extends Comparable<? super K>> T maxBy(List<T> items, Function<? super T, ? extends K> key) {
    return items.stream().max(Comparator.comparing(key)).orElseThrow();
}
```
```cpp
template <typename T> concept Addable = requires(T a, T b) { { a + b } -> std::same_as<T>; };
template <Addable T> T sum(const std::vector<T>& v, T init) { for (auto& x : v) init = init + x; return init; }
```
</details>
