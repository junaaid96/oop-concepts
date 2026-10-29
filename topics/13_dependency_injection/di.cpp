// Dependency injection in C++20: runtime (virtual interface) and compile-time (template) styles.
#include <cassert>
#include <iostream>
#include <map>
#include <memory>
#include <string>

struct Clock { virtual ~Clock() = default; virtual int hour() const = 0; };
struct FixedClock : Clock { int h; explicit FixedClock(int hh) : h(hh) {} int hour() const override { return h; } };

struct UserRepo { virtual ~UserRepo() = default; virtual std::string find(int id) const = 0; };
struct MapRepo : UserRepo {
    std::map<int, std::string> users;
    std::string find(int id) const override { auto it = users.find(id); return it == users.end() ? "guest" : it->second; }
};

class GreetingService {
public:
    // Constructor injection: dependencies are explicit and required.
    GreetingService(std::shared_ptr<const UserRepo> r, std::shared_ptr<const Clock> c)
        : repo_(std::move(r)), clock_(std::move(c)) {}
    std::string greet(int id) const {
        return std::string(clock_->hour() < 12 ? "Good morning, " : "Good evening, ") + repo_->find(id);
    }
private:
    std::shared_ptr<const UserRepo> repo_;
    std::shared_ptr<const Clock> clock_;
};

// Compile-time injection: the dependency is a template parameter (no virtual call).
template <typename ClockT>
class Alarm {
public:
    explicit Alarm(ClockT c) : clock_(c) {}
    bool shouldRing(int at) const { return clock_.hour() == at; }
private:
    ClockT clock_;
};

int main() {
    auto repo = std::make_shared<MapRepo>();
    repo->users[1] = "Ana";
    GreetingService svc(repo, std::make_shared<FixedClock>(9));    // composition root
    assert(svc.greet(1) == "Good morning, Ana");
    assert(svc.greet(2) == "Good morning, guest");
    assert(Alarm<FixedClock>(FixedClock(7)).shouldRing(7));
    std::cout << "di: all checks passed\n";
}
