// Value vs reference semantics, identity vs equality, and hashing in C++20.
#include <cassert>
#include <functional>
#include <iostream>
#include <memory>
#include <string>
#include <unordered_set>

struct Color {
    int r, g, b;
    bool operator==(const Color&) const = default;   // memberwise equality
};

// To use Color in unordered containers, provide a hash consistent with ==.
struct ColorHash {
    std::size_t operator()(const Color& c) const noexcept {
        std::size_t h = std::hash<int>{}(c.r);
        h = h * 31 + std::hash<int>{}(c.g);           // combine field hashes
        return h * 31 + std::hash<int>{}(c.b);
    }
};

int main() {
    Color a{1, 2, 3};
    Color b = a;                     // value semantics: b is an independent copy
    b.r = 9;
    assert(a.r == 1);                // a unchanged

    Color& ref = a;                  // reference: an alias for the same object
    ref.r = 7;
    assert(a.r == 7 && &ref == &a);  // identity: same address

    auto p1 = std::make_shared<Color>(Color{1, 1, 1});
    auto p2 = p1;                    // shared ownership: both point to ONE object
    p2->g = 5;
    assert(p1->g == 5 && p1 == p2);  // pointer equality = identity
    auto p3 = std::make_shared<Color>(Color{1, 5, 1});
    assert(p1 != p3 && *p1 == *p3);  // different identity, equal value

    std::unordered_set<Color, ColorHash> set{Color{1, 2, 3}, Color{1, 2, 3}};
    assert(set.size() == 1);
    std::cout << "equality: all checks passed\n";
}
