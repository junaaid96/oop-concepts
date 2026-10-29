# 08 · Object Lifecycle & Memory

> Files: [`Lifecycle.java`](src/main/java/oop/lifecycle/Lifecycle.java) · [`lifecycle.cpp`](lifecycle.cpp) · tests: [`LifecycleTest.java`](src/test/java/oop/lifecycle/LifecycleTest.java)

Lifecycle: **allocate → construct → use → destroy → free**.

**C++** — deterministic destruction. Objects on the stack die at the end of their scope; heap
objects are owned by smart pointers:
- **RAII**: acquire a resource in the constructor, release it in the destructor. Exceptions can't leak it.
- `std::unique_ptr` (single owner, zero overhead), `std::shared_ptr` (ref-counted),
  `std::weak_ptr` (non-owning, breaks cycles). Avoid raw `new`/`delete`.
- Move semantics transfer ownership instead of copying.

**Java** — a tracing garbage collector frees unreachable objects, *including cycles*; there are no
destructors and `finalize()` is deprecated for removal. Release non-memory resources with
**`AutoCloseable` + try-with-resources**; use `java.lang.ref.Cleaner` only as a safety net.
`WeakReference` / `WeakHashMap` avoid keeping objects alive (caches, listeners).

## Interview questions
1. What is RAII and why does C++ not need `finally`?
2. `unique_ptr` vs `shared_ptr` vs `weak_ptr`?
3. How can a `shared_ptr` cycle leak memory?
4. Why is relying on `finalize()` or the GC for closing files a bad idea? What should you use?
5. Can two Java objects that reference each other leak? *No — the GC traces from roots, so unreachable cycles are collected.*
6. What does try-with-resources do with an exception thrown by `close()`? *It is added as a suppressed exception.*

## Exercises
1. Write a `Timer` that implements `AutoCloseable` and records elapsed time.
2. In C++, make a `FileHandle` RAII wrapper that is movable but not copyable.

<details><summary>Solutions</summary>

```java
final class Timer implements AutoCloseable {
    private final long start = System.nanoTime();
    long elapsedNanos;
    @Override public void close() { elapsedNanos = System.nanoTime() - start; }   // no checked exception
}
try (var t = new Timer()) { work(); }
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
