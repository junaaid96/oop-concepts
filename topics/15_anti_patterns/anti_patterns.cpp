// Anemic vs rich domain model, and primitive obsession vs a value object, in C++20.
#include <cassert>
#include <iostream>
#include <numeric>
#include <stdexcept>
#include <string>
#include <vector>

struct AnemicOrder { std::vector<double> items; std::string status = "new"; };  // anti-pattern: no rules

class RichOrder {                                     // fix: invariants enforced inside
public:
    void add(double price) {
        if (paid_) throw std::logic_error("order already paid");
        items_.push_back(price);
    }
    void pay() {
        if (items_.empty()) throw std::logic_error("empty order");
        paid_ = true;
    }
    bool paid() const { return paid_; }
    double total() const { return std::accumulate(items_.begin(), items_.end(), 0.0); }
private:
    std::vector<double> items_;
    bool paid_ = false;
};

class Email {                                         // fix for primitive obsession
public:
    explicit Email(std::string v) : value_(std::move(v)) {
        if (value_.find('@') == std::string::npos) throw std::invalid_argument("bad email");
    }
    const std::string& str() const { return value_; }
private:
    std::string value_;                               // an Email is valid by construction
};

int main() {
    AnemicOrder a;
    a.status = "paid";                                // nothing prevents an empty paid order
    assert(a.items.empty() && a.status == "paid");

    RichOrder o;
    bool threw = false;
    try { o.pay(); } catch (const std::logic_error&) { threw = true; }
    assert(threw);
    o.add(10); o.pay();
    assert(o.paid() && o.total() == 10);

    assert(Email("a@b.c").str() == "a@b.c");
    std::cout << "anti_patterns: all checks passed\n";
}
