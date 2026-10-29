// Interfaces vs abstract classes in C++20.
#include <cassert>
#include <concepts>
#include <iostream>
#include <memory>
#include <string>

// "Interface": only pure virtual functions + virtual destructor, no data.
struct Printable {
    virtual ~Printable() = default;
    virtual std::string print() const = 0;
};

// Abstract class: has state, a constructor, a concrete template method and an abstract hook.
class Vehicle : public Printable {
public:
    explicit Vehicle(std::string brand) : brand_(std::move(brand)) {}
    std::string print() const override {                 // template method
        return brand_ + " with " + std::to_string(wheels()) + " wheels";
    }
protected:
    virtual int wheels() const = 0;                      // hook for subclasses
private:
    std::string brand_;
};

class Car : public Vehicle {
public:
    using Vehicle::Vehicle;                              // inherit constructor
protected:
    int wheels() const override { return 4; }
};

// A C++20 concept is a compile-time interface: no virtual calls, no inheritance.
template <typename T>
concept Exporter = requires(const T& e) { { e.exportData() } -> std::same_as<std::string>; };

struct CsvExporter { std::string exportData() const { return "a,b"; } };

template <Exporter E>
std::string saveReport(const E& e) { return "saved:" + e.exportData(); }

int main() {
    std::unique_ptr<Printable> p = std::make_unique<Car>("VW");  // used through the interface
    assert(p->print() == "VW with 4 wheels");
    assert(saveReport(CsvExporter{}) == "saved:a,b");
    static_assert(!std::is_abstract_v<Car> && std::is_abstract_v<Vehicle>);
    std::cout << "contracts: all checks passed\n";
}
