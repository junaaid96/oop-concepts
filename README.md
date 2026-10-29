# Object-Oriented Programming Concepts — from Basics to Design

[![CI](https://github.com/junaaid96/oop-concepts/actions/workflows/ci.yml/badge.svg)](https://github.com/junaaid96/oop-concepts/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A hands-on guide to object-oriented programming with **runnable, commented examples in C++20 and Python**,
tests for every topic, interview questions, exercises with solutions, and a detailed guide on
**[when to use OOP vs functional programming](docs/OOP_VS_FUNCTIONAL.md)**.

## Repository layout
```
basics/            # the original first-steps examples (class, object, methods, inheritance, ...)
topics/NN_topic/   # in-depth topics: README guide + .py + .cpp + tests
docs/              # cross-cutting guides (OOP vs FP)
scripts/run_all.sh # build every C++ example and run every test
```

## Learning path

### Stage 1 — Foundations (`basics/`)
| # | Concept | C++ | Python |
|---|---|---|---|
| 1 | Class | [1.02](basics/1.02_class.cpp) | [1.01](basics/1.01_class.py) |
| 2 | Object | [2.01](basics/2.01_object.cpp) | [2.02](basics/2.02_object.py) |
| 3 | Methods | [3.01](basics/3.01_methods.cpp) | [3.02](basics/3.02_methods.py) |
| 4 | Inheritance: single, multiple, multilevel, hierarchical, hybrid | [4.01](basics/4.01_single_inheritance.cpp) [4.03](basics/4.03_multiple_inheritance.cpp) [4.05](basics/4.05_multilevel_ingeritance.cpp) [4.07](basics/4.07_hierarchical_inheritance.cpp) [4.09](basics/4.09_hybrid_inheritance.cpp) | [4.02](basics/4.02_single_inheritance.py) [4.04](basics/4.04_multiple_inheritance.py) [4.06](basics/4.06_multilevel_inheritance.py) [4.08](basics/4.08_hierarchical_inheritance.py) [4.10](basics/4.10_hybrid_inheritance.py) |
| 5 | Encapsulation: public / private | [5.01](basics/5.01_public_encapsulation.cpp) [5.03](basics/5.03_private_encapsulation.cpp) | [5.02](basics/5.02_public_encapsulation.py) [5.04](basics/5.04_private_encapsulation.py) |
| 6 | Polymorphism: compile-time / runtime | [6.01](basics/6.01_compiletime_polymorphism.cpp) [6.03](basics/6.03_runtime_polymorphism.cpp) | [6.02](basics/6.02_compiletime_polymorphism.py) [6.04](basics/6.04_runtime_polymorphism.py) |
| 7 | Abstraction | [7.01](basics/7.01_abstraction.cpp) | [7.02](basics/7.02_abstraction.py) |

### Stage 2 — Core OOP in depth
| Topic | What you learn |
|---|---|
| [01 · Four pillars deepened](topics/01_four_pillars) | encapsulation vs abstraction, inheritance, **4 kinds of polymorphism** (subtype, ad hoc, parametric, coercion), duck typing |
| [02 · Classes & objects](topics/02_classes_objects) | constructors, alternative constructors, static members, `final`/`const`, immutability, rule of 0/3/5 |
| [03 · Interfaces vs abstract classes](topics/03_interfaces_vs_abstract) | Protocols, ABCs, C++ pure virtual & concepts, template method |
| [04 · Composition & delegation](topics/04_composition_delegation) | has-a vs is-a, swapping parts at runtime, `__getattr__` delegation |
| [05 · Generics](topics/05_generics) | `TypeVar`/`Generic`, bounds, variance, C++ templates & concepts |
| [06 · Enums, records & sealed types](topics/06_enums_records_sealed) | `Enum`, dataclasses, sum types, `match`, `std::variant` |
| [07 · Equality & identity](topics/07_equality_identity) | value vs reference, `==` vs `is`, hash contract, shallow/deep copy |
| [08 · Lifecycle & memory](topics/08_lifecycle_memory) | RAII, smart pointers, move semantics, context managers, GC, weakref |
| [09 · Exceptions in OOP](topics/09_exceptions) | exception hierarchies, chaining, exception-safety guarantees |
| [10 · Nested, inner & anonymous classes](topics/10_nested_inner_anonymous) | nested/inner/local classes, closures, lambdas |

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

Every topic README ends with **interview questions** and **exercises with solutions**.

## Running the examples
Requirements: a C++20 compiler (GCC 11+, Clang 14+, Apple Clang) and Python 3.10+.

```bash
./scripts/run_all.sh                                  # everything
c++ -std=c++20 topics/01_four_pillars/pillars.cpp -o pillars && ./pillars
cd topics/01_four_pillars && python3 -m unittest -v   # one topic's Python tests
```

## Contributing
See [CONTRIBUTING.md](CONTRIBUTING.md).

## License
[MIT](LICENSE)
