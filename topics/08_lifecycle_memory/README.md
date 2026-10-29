# 08 · Object Lifecycle & Memory

> Files: [`lifecycle.py`](lifecycle.py) · [`lifecycle.cpp`](lifecycle.cpp) · tests: [`test_lifecycle.py`](test_lifecycle.py)

Lifecycle: **allocate → construct → use → destroy → free**.

**C++** — deterministic destruction. Objects on the stack die at the end of their scope; heap
objects are owned by smart pointers:
- **RAII**: acquire a resource in the constructor, release it in the destructor. Exceptions can't leak it.
- `std::unique_ptr` (single owner, zero overhead), `std::shared_ptr` (ref-counted),
  `std::weak_ptr` (non-owning, breaks cycles). Avoid raw `new`/`delete`.
- Move semantics transfer ownership instead of copying.

**Python** — reference counting + a cycle-detecting garbage collector. `__del__` timing is not
guaranteed, so **don't** use it for important cleanup; use **context managers** (`with`) instead.
`weakref` avoids keeping objects alive (caches, observers).

## Interview questions
1. What is RAII and why does C++ not need `finally`?
2. `unique_ptr` vs `shared_ptr` vs `weak_ptr`?
3. How can a `shared_ptr` cycle leak memory?
4. Why is relying on `__del__` a bad idea? What should you use?
5. What happens to an object in Python when its reference count reaches zero?

## Exercises
1. Write a `Timer` context manager that records elapsed time.
2. In C++, make a `FileHandle` RAII wrapper that is movable but not copyable.

<details><summary>Solutions</summary>

```python
import time
class Timer:
    def __enter__(self): self.start = time.perf_counter(); return self
    def __exit__(self, *exc): self.elapsed = time.perf_counter() - self.start; return False
```
```cpp
class FileHandle {
  std::FILE* f_;
public:
  explicit FileHandle(const char* p) : f_(std::fopen(p, "r")) {}
  ~FileHandle() { if (f_) std::fclose(f_); }
  FileHandle(const FileHandle&) = delete;  FileHandle& operator=(const FileHandle&) = delete;
  FileHandle(FileHandle&& o) noexcept : f_(std::exchange(o.f_, nullptr)) {}
};
```
</details>
