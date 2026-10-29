// Classes, objects, constructors, static members, const/final and immutability in C++20.
#include <cassert>
#include <iostream>
#include <stdexcept>
#include <string>
#include <utility>

class User {
public:
    // Member-initializer list initialises fields directly (no default-then-assign).
    User(std::string name, std::string email) : name_(std::move(name)), email_(std::move(email)) {
        if (email_.find('@') == std::string::npos) throw std::invalid_argument("invalid email");
        ++count_;                                   // static counter shared by all Users
    }
    User(const User& other) : User(other.name_, other.email_) {}  // delegating constructor
    ~User() { --count_; }                           // destructor runs when the object dies
    User& operator=(const User&) = default;

    static User fromCsv(const std::string& line) {  // static factory = "alternative constructor"
        auto comma = line.find(',');
        return User(line.substr(0, comma), line.substr(comma + 1));
    }
    static int count() { return count_; }          // static function: no `this`

    const std::string& name() const { return name_; }  // const method: promises not to modify

private:
    std::string name_;
    std::string email_;
    inline static int count_ = 0;                  // C++17 inline static: defined in-class
};

// Immutable value object: all members const, "modifiers" return new objects.
class Money final {                                 // final: cannot be subclassed
public:
    explicit Money(long cents, std::string currency = "USD")
        : cents_(cents), currency_(std::move(currency)) {
        if (cents_ < 0) throw std::invalid_argument("negative amount");
    }
    Money add(const Money& o) const {
        if (o.currency_ != currency_) throw std::invalid_argument("currency mismatch");
        return Money(cents_ + o.cents_, currency_);  // new object, *this untouched
    }
    long cents() const { return cents_; }
    bool operator==(const Money&) const = default;
private:
    const long cents_;                              // const members: set once in constructor
    const std::string currency_;
};

int main() {
    {
        User a("Ana", "ana@example.com");
        User b = User::fromCsv("Bo,bo@example.com");
        assert(User::count() == 2);
        assert(b.name() == "Bo");
    }                                               // a and b destroyed here (scope ends)
    assert(User::count() == 0);

    const Money ten(1000);
    Money fifteen = ten.add(Money(500));
    assert(fifteen.cents() == 1500 && ten.cents() == 1000);
    assert(fifteen == Money(1500));

    bool threw = false;
    try { User bad("x", "nope"); } catch (const std::invalid_argument&) { threw = true; }
    assert(threw);
    std::cout << "classes: all checks passed\n";
}
