// Object lifecycle in C++: RAII, smart pointers, move semantics, weak_ptr.
#include <cassert>
#include <iostream>
#include <memory>
#include <stdexcept>
#include <string>
#include <utility>
#include <vector>

static std::vector<std::string> events;           // records lifecycle events for the asserts

class Resource {                                   // RAII wrapper
public:
    explicit Resource(std::string n) : name_(std::move(n)) { events.push_back("acquire " + name_); }
    ~Resource() { if (!name_.empty()) events.push_back("release " + name_); }   // runs automatically
    Resource(const Resource&) = delete;            // non-copyable: exactly one owner
    Resource& operator=(const Resource&) = delete;
    Resource(Resource&& o) noexcept : name_(std::exchange(o.name_, "")) {}      // movable
private:
    std::string name_;
};

void mayThrow() {
    Resource r("temp");                            // destroyed during stack unwinding
    throw std::runtime_error("fail");
}

struct Child;
struct Parent { std::shared_ptr<Child> child; ~Parent() { events.push_back("~Parent"); } };
struct Child  { std::weak_ptr<Parent> parent; ~Child() { events.push_back("~Child"); } };  // weak breaks the cycle

int main() {
    {
        Resource a("a");
        Resource b = std::move(a);                 // ownership moves, only ONE release later
    }
    assert((events == std::vector<std::string>{"acquire a", "release a"}));

    events.clear();
    try { mayThrow(); } catch (const std::runtime_error&) {}
    assert(events.back() == "release temp");       // no leak even with an exception

    events.clear();
    {
        auto p = std::make_shared<Parent>();
        p->child = std::make_shared<Child>();
        p->child->parent = p;                      // weak: doesn't bump the refcount
        assert(p.use_count() == 1);
    }
    assert(events.size() == 2);                    // both destroyed: no leak

    auto owner = std::make_unique<int>(42);        // unique ownership
    auto newOwner = std::move(owner);
    assert(!owner && *newOwner == 42);
    std::cout << "lifecycle: all checks passed\n";
}
