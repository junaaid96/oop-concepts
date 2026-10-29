// The four pillars of OOP in modern C++ (C++20).
// Build & run:  clang++ -std=c++20 -Wall -Wextra pillars.cpp -o pillars && ./pillars
// The asserts in main() act as tests: the program exits non-zero if one fails.
#include <cassert>
#include <cmath>
#include <memory>
#include <numbers>
#include <stdexcept>
#include <string>
#include <vector>
#include <iostream>

// ------------------------------------------------------------ Encapsulation
class BankAccount {
public:
    explicit BankAccount(std::string owner) : owner_(std::move(owner)) {}  // explicit: no implicit conversion from string

    double balance() const { return balance_; }            // read-only accessor (const = doesn't modify)

    void deposit(double amount) {
        if (amount <= 0) throw std::invalid_argument("deposit must be positive");  // guard invariant
        balance_ += amount;
    }
    void withdraw(double amount) {
        if (amount > balance_) throw std::runtime_error("insufficient funds");     // balance never negative
        balance_ -= amount;
    }

private:                                                    // state is hidden from callers
    std::string owner_;
    double balance_ = 0.0;
};

// ------------------------------------------- Abstraction + dynamic polymorphism
class Shape {
public:
    virtual ~Shape() = default;                 // virtual destructor: safe delete through Shape*
    virtual double area() const = 0;            // pure virtual = abstract method
    virtual std::string name() const = 0;
};

class Circle final : public Shape {             // final: nobody may derive from Circle
public:
    explicit Circle(double r) : r_(r) {}
    double area() const override { return std::numbers::pi * r_ * r_; }  // override checked by compiler
    std::string name() const override { return "Circle"; }
private:
    double r_;
};

class Rectangle final : public Shape {
public:
    Rectangle(double w, double h) : w_(w), h_(h) {}
    double area() const override { return w_ * h_; }
    std::string name() const override { return "Rectangle"; }
private:
    double w_, h_;
};

// Works with any Shape; which area() runs is decided at runtime through the vtable.
double totalArea(const std::vector<std::unique_ptr<Shape>>& shapes) {
    double sum = 0;
    for (const auto& s : shapes) sum += s->area();
    return sum;
}

// ------------------------------------------------------ Ad hoc polymorphism
struct Vector2 {
    double x, y;
    Vector2 operator+(const Vector2& o) const { return {x + o.x, y + o.y}; }  // operator overloading
    bool operator==(const Vector2&) const = default;                         // C++20 defaulted equality
};

std::string describe(int)                { return "int"; }        // function overloading:
std::string describe(double)             { return "double"; }     // the compiler picks the
std::string describe(const std::string&) { return "string"; }     // best match at compile time

// ---------------------------------------------------- Parametric polymorphism
template <typename T>                       // one definition, instantiated per type
class Stack {
public:
    void push(T item) { items_.push_back(std::move(item)); }
    T pop() { T top = std::move(items_.back()); items_.pop_back(); return top; }
    std::size_t size() const { return items_.size(); }
private:
    std::vector<T> items_;
};

template <typename T>
concept HasArea = requires(const T& t) { { t.area() } -> std::convertible_to<double>; };  // C++20 concept

template <HasArea T>                         // static ("compile-time duck typing") polymorphism
double doubledArea(const T& t) { return 2 * t.area(); }

int main() {
    BankAccount acc("Ana");
    acc.deposit(100);
    acc.withdraw(30);
    assert(acc.balance() == 70);
    bool threw = false;
    try { acc.withdraw(1000); } catch (const std::runtime_error&) { threw = true; }
    assert(threw);                                       // invariant protected

    std::vector<std::unique_ptr<Shape>> shapes;
    shapes.push_back(std::make_unique<Circle>(1));
    shapes.push_back(std::make_unique<Rectangle>(2, 3));
    assert(std::abs(totalArea(shapes) - (std::numbers::pi + 6)) < 1e-9);

    assert((Vector2{1, 2} + Vector2{3, 4} == Vector2{4, 6}));
    assert(describe(1) == "int" && describe(1.0) == "double" && describe(std::string("x")) == "string");

    Stack<std::string> s;
    s.push("hello");
    assert(s.size() == 1 && s.pop() == "hello");
    assert(doubledArea(Rectangle(1, 2)) == 4);

    std::cout << "pillars: all checks passed\n";
}
