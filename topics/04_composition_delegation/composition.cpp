// Composition and delegation in C++20.
#include <cassert>
#include <iostream>
#include <memory>
#include <string>
#include <vector>

struct Engine {                                   // the "part" interface
    virtual ~Engine() = default;
    virtual std::string start() const = 0;
};
struct PetrolEngine : Engine { std::string start() const override { return "vroom"; } };
struct ElectricEngine : Engine { std::string start() const override { return "hum"; } };

class Car {
public:
    explicit Car(std::unique_ptr<Engine> e) : engine_(std::move(e)) {}  // owns its part
    void swapEngine(std::unique_ptr<Engine> e) { engine_ = std::move(e); }  // runtime change
    std::string start() const { return "car: " + engine_->start(); }      // delegation
private:
    std::unique_ptr<Engine> engine_;              // composition: Car HAS-AN Engine
};

template <typename T>
class Stack {                                     // wraps vector instead of inheriting from it
public:
    void push(T v) { data_.push_back(std::move(v)); }
    T pop() { T v = data_.back(); data_.pop_back(); return v; }
    bool empty() const { return data_.empty(); }
private:
    std::vector<T> data_;                         // std containers have no virtual dtor: never inherit publicly
};

int main() {
    Car car(std::make_unique<PetrolEngine>());
    assert(car.start() == "car: vroom");
    car.swapEngine(std::make_unique<ElectricEngine>());
    assert(car.start() == "car: hum");

    Stack<int> s;
    s.push(1); s.push(2);
    assert(s.pop() == 2 && !s.empty());
    std::cout << "composition: all checks passed\n";
}
