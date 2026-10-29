// SOLID in C++20: an invoicing example.
#include <algorithm>
#include <cassert>
#include <iostream>
#include <memory>
#include <string>
#include <utility>
#include <vector>

struct LineItem { std::string name; double price; int qty; };

struct Discount {                                    // O: extension point
    virtual ~Discount() = default;
    virtual double apply(double total) const = 0;
};
struct FlatDiscount : Discount {
    explicit FlatDiscount(double a) : amount(a) {}
    double apply(double t) const override { return std::max(0.0, t - amount); }
    double amount;
};

class InvoiceCalculator {                           // S: only pricing
public:
    void addDiscount(std::unique_ptr<Discount> d) { discounts_.push_back(std::move(d)); }
    double total(const std::vector<LineItem>& items) const {
        double sum = 0;
        for (const auto& i : items) sum += i.price * i.qty;
        for (const auto& d : discounts_) sum = d->apply(sum);
        return sum;
    }
private:
    std::vector<std::unique_ptr<Discount>> discounts_;
};

struct Sender {                                      // I: tiny interface
    virtual ~Sender() = default;
    virtual void send(const std::string& to, const std::string& body) = 0;
};

class InvoiceService {                              // D: depends on abstractions only
public:
    InvoiceService(const InvoiceCalculator& c, Sender& s) : calc_(c), sender_(s) {}
    double bill(const std::string& to, const std::vector<LineItem>& items) {
        double t = calc_.total(items);
        sender_.send(to, "TOTAL " + std::to_string(t));
        return t;
    }
private:
    const InvoiceCalculator& calc_;
    Sender& sender_;
};

struct FakeSender : Sender {                         // test double
    std::vector<std::string> sent;
    void send(const std::string&, const std::string& body) override { sent.push_back(body); }
};

int main() {
    std::vector<LineItem> items{{"pen", 2.5, 4}, {"book", 10, 1}};
    InvoiceCalculator calc;
    assert(calc.total(items) == 20);
    calc.addDiscount(std::make_unique<FlatDiscount>(5));
    FakeSender fake;
    InvoiceService svc(calc, fake);
    assert(svc.bill("ana", items) == 15 && fake.sent.size() == 1);
    std::cout << "solid: all checks passed\n";
}
