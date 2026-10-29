# 03 · Interfaces vs Abstract Classes

> Files: [`contracts.py`](contracts.py) · [`contracts.cpp`](contracts.cpp) · tests: [`test_contracts.py`](test_contracts.py)

| | Interface | Abstract class |
|---|---|---|
| Purpose | A **capability / contract** ("can be saved", "can fly") | A **partial implementation** of a family ("is a vehicle") |
| State | none | can hold fields |
| Implementation | none (or default methods) | some concrete methods + some abstract ones |
| How many | a class may implement many | usually single base |
| Python | `typing.Protocol` (structural) or ABC with only abstract methods | `abc.ABC` with `@abstractmethod` + concrete methods |
| C++ | class with only pure virtual functions (+ virtual dtor), or a C++20 `concept` | class with some pure virtual and some implemented members |

**Rule of thumb:** depend on small interfaces; use an abstract class to share code between
closely related implementations (e.g. a *template method*).

## Interview questions
1. When would you pick an abstract class over an interface?
2. What is the difference between `abc.ABC` (nominal) and `Protocol` (structural) typing?
3. Can an abstract class have a constructor? *Yes — to initialise shared state; it runs from subclass constructors.*
4. What is the Template Method pattern?
5. What is the Interface Segregation Principle?

## Exercises
1. Add a `CsvExporter` implementing `Exporter`.
2. Add a `Truck` subclass of `Vehicle` with 6 wheels.

<details><summary>Solutions</summary>

```python
class CsvExporter:                  # satisfies Exporter structurally — no inheritance needed
    def export(self, rows): return "\n".join(",".join(map(str, r)) for r in rows)

class Truck(Vehicle):
    def wheels(self): return 6
```
</details>
