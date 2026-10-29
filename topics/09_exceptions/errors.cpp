// Exception hierarchies and exception safety in C++20.
#include <cassert>
#include <iostream>
#include <stdexcept>
#include <string>
#include <vector>

struct AppError : std::runtime_error { using std::runtime_error::runtime_error; };
struct PaymentError : AppError { using AppError::AppError; };
struct InsufficientFunds : PaymentError {
    InsufficientFunds(double need, double have)
        : PaymentError("needed " + std::to_string(need) + ", have " + std::to_string(have)), needed(need) {}
    double needed;
};

class Ledger {
public:
    // Strong guarantee via copy-and-swap: work on a copy, then swap with a noexcept operation.
    void addAll(const std::vector<int>& amounts) {
        std::vector<int> copy = entries_;
        for (int a : amounts) {
            if (a == 0) throw std::invalid_argument("zero entry");  // throws before touching entries_
            copy.push_back(a);
        }
        entries_.swap(copy);                       // swap never throws: commit point
    }
    std::size_t size() const noexcept { return entries_.size(); }
private:
    std::vector<int> entries_;
};

void pay(double balance, double amount) {
    if (amount > balance) throw InsufficientFunds(amount, balance);
}

int main() {
    try {
        pay(5, 10);
        assert(false);
    } catch (const PaymentError& e) {              // catch by const reference to the base
        auto* specific = dynamic_cast<const InsufficientFunds*>(&e);
        assert(specific && specific->needed == 10);
    }

    Ledger l;
    l.addAll({1, 2});
    try { l.addAll({3, 0}); } catch (const std::invalid_argument&) {}
    assert(l.size() == 2);                         // unchanged after failure

    try {
        try { throw std::runtime_error("socket closed"); }
        catch (...) { std::throw_with_nested(AppError("gateway down")); }  // chain the cause
    } catch (const AppError& e) {
        bool sawCause = false;
        try { std::rethrow_if_nested(e); } catch (const std::runtime_error& inner) {
            sawCause = std::string(inner.what()) == "socket closed";
        }
        assert(sawCause);
    }
    std::cout << "errors: all checks passed\n";
}
