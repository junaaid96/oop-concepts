// Templates and C++20 concepts.
#include <cassert>
#include <concepts>
#include <iostream>
#include <map>
#include <optional>
#include <string>
#include <vector>

template <typename T>
concept LessComparable = requires(const T& a, const T& b) { { a < b } -> std::convertible_to<bool>; };

template <LessComparable T>                        // constrained template
const T& smallest(const std::vector<T>& v) {
    const T* best = &v.front();
    for (const auto& x : v) if (x < *best) best = &x;
    return *best;
}

template <typename T>
class Box {
public:
    explicit Box(T v) : value_(std::move(v)) {}
    template <typename F>
    auto map(F fn) const -> Box<decltype(fn(std::declval<T>()))> {  // result type deduced
        return Box<decltype(fn(value_))>(fn(value_));
    }
    const T& get() const { return value_; }
private:
    T value_;
};

template <typename T, typename Id = int>           // default template argument
class Repository {
public:
    void add(Id id, T item) { items_.insert_or_assign(id, std::move(item)); }
    std::optional<T> get(Id id) const {
        auto it = items_.find(id);
        return it == items_.end() ? std::nullopt : std::optional<T>(it->second);
    }
private:
    std::map<Id, T> items_;
};

int main() {
    assert(smallest(std::vector<int>{3, 1, 2}) == 1);
    assert(smallest(std::vector<std::string>{"b", "a"}) == "a");
    auto b = Box(21).map([](int x) { return x * 2; }).map([](int x) { return std::to_string(x); });
    assert(b.get() == "42");
    Repository<std::string> repo;
    repo.add(1, "Ana");
    assert(repo.get(1).value() == "Ana" && !repo.get(2).has_value());
    std::cout << "generics: all checks passed\n";
}
