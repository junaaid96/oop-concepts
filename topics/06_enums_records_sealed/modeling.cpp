// enum class, record-like structs and a sealed sum type with std::variant.
#include <cassert>
#include <cmath>
#include <iostream>
#include <numbers>
#include <string_view>
#include <variant>

enum class OrderStatus { Pending, Paid, Shipped, Cancelled };   // scoped: no implicit int

constexpr bool isFinal(OrderStatus s) {
    switch (s) {                                // -Wall warns if an enumerator is missing
        case OrderStatus::Shipped:
        case OrderStatus::Cancelled: return true;
        case OrderStatus::Pending:
        case OrderStatus::Paid: return false;
    }
    return false;
}

struct Point {                                  // record: plain data + generated comparisons
    double x, y;
    auto operator<=>(const Point&) const = default;
};

struct Circle { double radius; };
struct Square { double side; };
using Shape = std::variant<Circle, Square>;     // closed set of alternatives

template <class... Fs> struct overloaded : Fs... { using Fs::operator()...; };  // visitor helper

double area(const Shape& s) {
    return std::visit(overloaded{                // compile error if an alternative is unhandled
        [](const Circle& c) { return std::numbers::pi * c.radius * c.radius; },
        [](const Square& q) { return q.side * q.side; },
    }, s);
}

int main() {
    static_assert(isFinal(OrderStatus::Shipped) && !isFinal(OrderStatus::Paid));
    assert((Point{1, 2} == Point{1, 2}) && (Point{1, 2} < Point{2, 0}));
    assert(std::abs(area(Circle{1}) - std::numbers::pi) < 1e-9);
    assert(area(Square{3}) == 9);
    std::cout << "modeling: all checks passed\n";
}
