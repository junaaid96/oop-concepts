// Design by contract in C++20 with small macros (C++26 adds native pre/post contracts).
#include <cassert>
#include <iostream>
#include <stdexcept>
#include <string>
#include <vector>

#define REQUIRE(cond) do { if (!(cond)) throw std::logic_error("precondition: " #cond); } while (0)
#define ENSURE(cond)  do { if (!(cond)) throw std::logic_error("postcondition: " #cond); } while (0)

class SortedList {
public:
    void insert(int v) {
        auto old = items_.size();                       // capture "old" state for the postcondition
        auto pos = items_.begin();
        while (pos != items_.end() && *pos < v) ++pos;
        items_.insert(pos, v);
        ENSURE(items_.size() == old + 1);
        checkInvariant();
    }
    int at(std::size_t i) const {
        REQUIRE(i < items_.size());                     // caller's obligation
        return items_[i];
    }
private:
    void checkInvariant() const {                       // class invariant: always sorted
        for (std::size_t i = 1; i < items_.size(); ++i)
            if (items_[i - 1] > items_[i]) throw std::logic_error("invariant: sorted");
    }
    std::vector<int> items_;
};

int main() {
    SortedList s;
    s.insert(3); s.insert(1); s.insert(2);
    assert(s.at(0) == 1 && s.at(2) == 3);
    bool threw = false;
    try { s.at(10); } catch (const std::logic_error&) { threw = true; }
    assert(threw);
    std::cout << "dbc: all checks passed\n";
}
