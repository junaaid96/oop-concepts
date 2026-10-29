// Strategy, Factory, Decorator, Builder, Observer and Command in C++20.
#include <cassert>
#include <functional>
#include <iostream>
#include <map>
#include <memory>
#include <string>
#include <vector>

// Strategy
struct Shipping { virtual ~Shipping() = default; virtual double cost(double kg) const = 0; };
struct Standard : Shipping { double cost(double kg) const override { return 5 + kg; } };
struct Free : Shipping { double cost(double) const override { return 0; } };

// Factory + Decorator
struct Notifier { virtual ~Notifier() = default; virtual std::string send(const std::string&) = 0; };
struct Email : Notifier { std::string send(const std::string& m) override { return "email:" + m; } };
struct Sms : Notifier { std::string send(const std::string& m) override { return "sms:" + m; } };
std::unique_ptr<Notifier> makeNotifier(const std::string& kind) {
    if (kind == "sms") return std::make_unique<Sms>();
    return std::make_unique<Email>();
}
class Logging : public Notifier {                         // decorator: wraps and forwards
public:
    Logging(std::unique_ptr<Notifier> inner, std::vector<std::string>& log) : inner_(std::move(inner)), log_(log) {}
    std::string send(const std::string& m) override { log_.push_back("sending " + m); return inner_->send(m); }
private:
    std::unique_ptr<Notifier> inner_;
    std::vector<std::string>& log_;
};

// Builder
struct Pizza { std::string size; std::vector<std::string> toppings; };
class PizzaBuilder {
public:
    PizzaBuilder& size(std::string s) { p_.size = std::move(s); return *this; }   // fluent
    PizzaBuilder& topping(std::string t) { p_.toppings.push_back(std::move(t)); return *this; }
    Pizza build() const { return p_; }
private:
    Pizza p_{"medium", {}};
};

// Observer
class EventBus {
public:
    using Handler = std::function<void(int)>;
    void subscribe(const std::string& e, Handler h) { subs_[e].push_back(std::move(h)); }
    void publish(const std::string& e, int payload) { for (auto& h : subs_[e]) h(payload); }
private:
    std::map<std::string, std::vector<Handler>> subs_;
};

// Command with undo
struct Document { std::string text; };
struct AddText {
    std::string value;
    void execute(Document& d) const { d.text += value; }
    void undo(Document& d) const { d.text.resize(d.text.size() - value.size()); }
};

int main() {
    std::unique_ptr<Shipping> s = std::make_unique<Standard>();
    assert(s->cost(2) == 7);
    s = std::make_unique<Free>();                       // swap strategy at runtime
    assert(s->cost(2) == 0);

    std::vector<std::string> log;
    Logging n(makeNotifier("sms"), log);
    assert(n.send("hi") == "sms:hi" && log.size() == 1);

    auto p = PizzaBuilder().size("large").topping("olive").build();
    assert(p.size == "large" && p.toppings.size() == 1);

    EventBus bus; int got = 0;
    bus.subscribe("order", [&got](int v) { got = v; });
    bus.publish("order", 42);
    assert(got == 42);

    Document d; std::vector<AddText> history{{"Hello"}, {" world"}};
    for (auto& c : history) c.execute(d);
    history.back().undo(d);
    assert(d.text == "Hello");
    std::cout << "patterns: all checks passed\n";
}
