// The same problems solved with OOP and with a functional style in C++20/23-ready code.
#include <algorithm>
#include <cassert>
#include <cmath>
#include <functional>
#include <iostream>
#include <map>
#include <numbers>
#include <numeric>
#include <optional>
#include <ranges>
#include <stdexcept>
#include <string>
#include <type_traits>
#include <variant>
#include <vector>

struct Item { std::string name; double price; int qty; };

// ---- 1. Cart: OOP ----------------------------------------------------------------
class Cart {
public:
    void add(Item i) { items_.push_back(std::move(i)); }       // mutates internal state
    double total() const {
        double t = 0;
        for (const auto& i : items_) t += i.price * i.qty;
        return t;
    }
private:
    std::vector<Item> items_;
};

// ---- 1. Cart: FP — ranges pipeline over immutable input --------------------------
double cartTotal(const std::vector<Item>& items) {
    auto lines = items | std::views::transform([](const Item& i) { return i.price * i.qty; });
    return std::accumulate(lines.begin(), lines.end(), 0.0);   // fold
}

// Higher-order function: returns a new function.
auto discount(double pct) { return [pct](double x) { return x * (1 - pct / 100); }; }

// ---- 2. Shapes: OOP (virtual) vs FP (variant + visit) ----------------------------
struct ShapeOOP { virtual ~ShapeOOP() = default; virtual double area() const = 0; };
struct CircleOOP : ShapeOOP { double r; explicit CircleOOP(double rr) : r(rr) {} double area() const override { return std::numbers::pi * r * r; } };

struct Circle { double r; };
struct Square { double s; };
using Shape = std::variant<Circle, Square>;
double area(const Shape& sh) {
    return std::visit([](const auto& x) -> double {
        if constexpr (std::is_same_v<std::decay_t<decltype(x)>, Circle>) return std::numbers::pi * x.r * x.r;
        else return x.s * x.s;
    }, sh);
}

// ---- 3. Failure: exceptions vs optional chaining (monadic and_then in C++23) -----
using Stock = std::map<std::string, int>;
std::optional<Stock> placeOrder(Stock stock, const std::string& name, int qty) {  // takes a copy: pure
    if (qty <= 0 || stock[name] < qty) return std::nullopt;       // failure is a value, not a throw
    stock[name] -= qty;
    return stock;
}

// ---- 4. Account: object vs fold over events ---------------------------------------
class Account {
public:
    void deposit(double a) { balance_ += a; }
    void withdraw(double a) { if (a > balance_) throw std::runtime_error("insufficient"); balance_ -= a; }
    double balance() const { return balance_; }
private:
    double balance_ = 0;
};
struct Event { bool deposit; double amount; };
double balanceFrom(const std::vector<Event>& evs) {
    return std::accumulate(evs.begin(), evs.end(), 0.0,
                           [](double b, const Event& e) { return e.deposit ? b + e.amount : b - e.amount; });
}

// ---- 5. Strategy: interface vs std::function -------------------------------------
double checkout(double base, const std::function<double(double)>& pricing) { return pricing(base); }

int main() {
    std::vector<Item> items{{"pen", 2.5, 4}, {"book", 10, 1}};
    Cart cart;
    for (const auto& i : items) cart.add(i);
    assert(cart.total() == cartTotal(items));
    assert(discount(10)(cartTotal(items)) == 18);

    assert(std::abs(CircleOOP(1).area() - area(Circle{1})) < 1e-12);
    assert(area(Square{3}) == 9);

    Stock stock{{"pen", 3}};
    auto after = placeOrder(stock, "pen", 2);
    assert(after && after->at("pen") == 1 && stock.at("pen") == 3);   // original untouched
    assert(!placeOrder(stock, "pen", 9));

    Account acc; acc.deposit(100); acc.withdraw(30);
    assert(acc.balance() == balanceFrom({{true, 100}, {false, 30}}));

    assert(checkout(100, [](double b) { return b * 0.5; }) == 50);
    std::cout << "oop_vs_fp: all checks passed\n";
}
