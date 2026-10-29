# 04 · Composition vs Inheritance, and Delegation

> Files: [`composition.py`](composition.py) · [`composition.cpp`](composition.cpp) · tests: [`test_composition.py`](test_composition.py)

**"Favour composition over inheritance"** (Gang of Four). Composition = an object *has* other
objects and uses them ("has-a"). Inheritance = an object *is* a specialised kind of another ("is-a").

| Use inheritance when | Use composition when |
|---|---|
| true is-a, substitutable (LSP holds) | you want to reuse behaviour only |
| hierarchy is shallow and stable | behaviour must change at runtime |
| framework requires it (e.g. `unittest.TestCase`) | you need many combinations (avoid class explosion) |

**Delegation**: the outer object forwards a request to a helper it owns. It gives you reuse
without inheritance's tight coupling (`__getattr__` in Python can automate forwarding).

**Classic trap**: `class Stack(list)` exposes `insert`, `sort`… which break the stack contract.
Wrap a list instead.

## Interview questions
1. Why is inheritance called "white-box reuse" and composition "black-box reuse"?
2. What is the fragile base class problem?
3. Is `Square extends Rectangle` a good idea? *No — setting width independently breaks LSP.*
4. How do you swap behaviour at runtime? *Inject a different strategy object.*

## Exercises
1. Add a `TurboEngine` and give a car one at runtime.
2. Implement a `LoggingList` that delegates to a list but logs every `append`.

<details><summary>Solutions</summary>

```python
class TurboEngine:
    def start(self): return "turbo whoosh"
car = Car(ElectricEngine()); car.engine = TurboEngine()

class LoggingList:
    def __init__(self): self._items, self.log = [], []
    def append(self, x): self.log.append(f"append {x}"); self._items.append(x)
    def __getattr__(self, name): return getattr(self._items, name)   # delegate the rest
```
</details>
