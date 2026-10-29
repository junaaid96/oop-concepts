# Object-Oriented Programming Concepts — from Basics to Design

[![CI](https://github.com/junaaid96/oop-concepts/actions/workflows/ci.yml/badge.svg)](https://github.com/junaaid96/oop-concepts/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A hands-on guide to object-oriented programming with **runnable, commented examples in C++20 and Java 25**,
tests for every topic, interview questions, exercises with solutions, and a detailed guide on
**[when to use OOP vs functional programming](docs/OOP_VS_FUNCTIONAL.md)**, plus a
**[C++ vs Java OOP comparison](docs/CPP_VS_JAVA_OOP.md)**.

## Repository layout
```
pom.xml            # Maven parent (Java 25, JUnit 5); ./mvnw is the Maven wrapper
basics/            # first-steps examples: C++ files + a Java module (src/main/java, src/test/java)
topics/NN_topic/   # in-depth topics: README guide + .cpp + Maven module with Java code and JUnit tests
docs/              # cross-cutting guides (OOP vs FP, C++ vs Java)
scripts/run_all.sh # build and run every C++ example, then ./mvnw verify
```

## Learning path

### Stage 1 — Foundations (`basics/`)
| # | Concept | C++ | Java |
|---|---|---|---|
| 1 | Class | [1.02](basics/1.02_class.cpp) | [ClassDemo](basics/src/main/java/oop/basics/ClassDemo.java) |
| 2 | Object | [2.01](basics/2.01_object.cpp) | [ClassDemo](basics/src/main/java/oop/basics/ClassDemo.java) |
| 3 | Methods | [3.01](basics/3.01_methods.cpp) | [ClassDemo](basics/src/main/java/oop/basics/ClassDemo.java) |
| 4 | Inheritance: single, multiple, multilevel, hierarchical, hybrid | [4.01](basics/4.01_single_inheritance.cpp) [4.03](basics/4.03_multiple_inheritance.cpp) [4.05](basics/4.05_multilevel_ingeritance.cpp) [4.07](basics/4.07_hierarchical_inheritance.cpp) [4.09](basics/4.09_hybrid_inheritance.cpp) | [InheritanceDemo](basics/src/main/java/oop/basics/InheritanceDemo.java) (multiple/hybrid via interfaces) |
| 5 | Encapsulation: public / private | [5.01](basics/5.01_public_encapsulation.cpp) [5.03](basics/5.03_private_encapsulation.cpp) | [EncapsulationDemo](basics/src/main/java/oop/basics/EncapsulationDemo.java) |
| 6 | Polymorphism: compile-time / runtime | [6.01](basics/6.01_compiletime_polymorphism.cpp) [6.03](basics/6.03_runtime_polymorphism.cpp) | [PolymorphismDemo](basics/src/main/java/oop/basics/PolymorphismDemo.java) |
| 7 | Abstraction | [7.01](basics/7.01_abstraction.cpp) | [AbstractionDemo](basics/src/main/java/oop/basics/AbstractionDemo.java) |

Tests for the Java basics: [BasicsTest](basics/src/test/java/oop/basics/BasicsTest.java).

### Stage 2 — Core OOP in depth
| Topic | What you learn |
|---|---|
| [01 · Four pillars deepened](topics/01_four_pillars) | encapsulation vs abstraction, inheritance, **4 kinds of polymorphism** (subtype, ad hoc, parametric, coercion), pattern `switch` |
| [02 · Classes & objects](topics/02_classes_objects) | constructors, alternative constructors, static members, `final`/`const`, records, immutability, JEP 513 flexible constructors, rule of 0/3/5 |
| [03 · Interfaces vs abstract classes](topics/03_interfaces_vs_abstract) | default/static/private interface methods, diamond resolution, C++ pure virtual & concepts, template method |
| [04 · Composition & delegation](topics/04_composition_delegation) | has-a vs is-a, swapping parts at runtime, forwarding, why `Stack extends ArrayList` is a trap |
| [05 · Generics](topics/05_generics) | bounded types, wildcards & PECS, type erasure, C++ templates & concepts |
| [06 · Enums, records & sealed types](topics/06_enums_records_sealed) | rich enums, records, sealed interfaces, exhaustive `switch`, record patterns, `std::variant` |
| [07 · Equality & identity](topics/07_equality_identity) | value vs reference, `==` vs `equals`, `equals`/`hashCode` contract, defensive copies |
| [08 · Lifecycle & memory](topics/08_lifecycle_memory) | RAII, smart pointers, move semantics, try-with-resources, GC, `Cleaner`, `WeakReference` |
| [09 · Exceptions in OOP](topics/09_exceptions) | exception hierarchies, checked vs unchecked, chaining, suppressed, exception-safety guarantees |
| [10 · Nested, inner & anonymous classes](topics/10_nested_inner_anonymous) | static nested/inner/local/anonymous classes, closures, lambdas |

### Stage 3 — Design
| Topic | What you learn |
|---|---|
| [11 · SOLID](topics/11_solid) | all five principles on one invoicing example, with further-reading links |
| [12 · Design by contract](topics/12_design_by_contract) | preconditions, postconditions, invariants |
| [13 · Dependency injection](topics/13_dependency_injection) | constructor/setter/parameter injection, composition root, a tiny container |
| [14 · Design patterns](topics/14_design_patterns) | Strategy, Factory, Builder, Adapter, Decorator, Observer, Command |
| [15 · Anti-patterns](topics/15_anti_patterns) | god object, anemic domain model, deep inheritance, primitive obsession |

### Stage 4 — Paradigms
| Topic | What you learn |
|---|---|
| [16 · OOP vs FP side by side](topics/16_oop_vs_functional) | six problems solved both ways, with tests |
| [**Guide: OOP vs Functional**](docs/OOP_VS_FUNCTIONAL.md) | when each shines, hybrid designs, monads, side effects, decision flowchart, cheat sheet |
| [**Guide: C++ vs Java OOP**](docs/CPP_VS_JAVA_OOP.md) | virtual/override/final, destructors, slicing, multiple inheritance, copy/move/RAII, memory model, templates vs generics |

Every topic README ends with **interview questions** and **exercises with solutions**.

## Running the examples
Requirements: a C++20 compiler (GCC 11+, Clang 14+, Apple Clang) and **JDK 25** (e.g. Temurin 25).
Maven itself is not needed — the wrapper `./mvnw` downloads it.

```bash
./scripts/run_all.sh                                   # everything: C++ examples + ./mvnw verify
./mvnw verify                                          # all Java code and JUnit 5 tests
./mvnw -pl topics/01_four_pillars test                 # one topic's Java tests
java topics/01_four_pillars/src/main/java/oop/pillars/Pillars.java   # run an example that has a main()
c++ -std=c++20 topics/01_four_pillars/pillars.cpp -o pillars && ./pillars
```

## Contributing
See [CONTRIBUTING.md](CONTRIBUTING.md).

## License
[MIT](LICENSE)
