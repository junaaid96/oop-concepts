// Nested, "inner", local and anonymous classes plus lambdas in C++20.
#include <cassert>
#include <functional>
#include <iostream>
#include <memory>
#include <string>
#include <vector>

class LinkedList {
    struct Node {                                  // nested & private: hidden from users
        int value;
        std::unique_ptr<Node> next;
    };
    std::unique_ptr<Node> head_;
public:
    void pushFront(int v) { head_ = std::make_unique<Node>(Node{v, std::move(head_)}); }
    std::vector<int> toVector() const {
        std::vector<int> out;
        for (Node* n = head_.get(); n; n = n->next.get()) out.push_back(n->value);
        return out;
    }
};

class Button {
public:
    class Clicker {                                // "inner" class: holds a pointer to its outer object
    public:
        explicit Clicker(Button& b) : outer_(&b) {}
        void click() { ++outer_->clicks_; }        // nested classes may access private members
    private:
        Button* outer_;
    };
    Clicker clicker() { return Clicker(*this); }
    int clicks() const { return clicks_; }
private:
    int clicks_ = 0;
};

std::function<bool(const std::string&)> makeValidator(std::size_t minLen) {
    struct LengthRule {                            // local class
        std::size_t min;
        bool operator()(const std::string& s) const { return s.size() >= min; }
    };
    return LengthRule{minLen};
}

int main() {
    LinkedList ll;
    ll.pushFront(3); ll.pushFront(2); ll.pushFront(1);
    assert((ll.toVector() == std::vector<int>{1, 2, 3}));

    Button b;
    b.clicker().click(); b.clicker().click();
    assert(b.clicks() == 2);

    auto atLeast3 = makeValidator(3);
    assert(atLeast3("abc") && !atLeast3("ab"));

    int base = 10;
    auto addBase = [base](int x) { return x + base; };   // lambda = anonymous function object; captures by value
    struct { int operator()(int x) const { return x * 2; } } twice;  // anonymous struct
    assert(addBase(1) == 11 && twice(4) == 8);
    std::cout << "nested: all checks passed\n";
}
