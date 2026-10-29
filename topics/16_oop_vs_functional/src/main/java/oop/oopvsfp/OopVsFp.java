package oop.oopvsfp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** The same problems solved in an object-oriented way and in a functional way. */
public final class OopVsFp {
    private OopVsFp() {}

    public record Item(String name, double price, int qty) {}

    // =============================== 1. Cart total ===================================
    public static final class Cart {                         // OOP: state + behaviour together
        private final List<Item> items = new ArrayList<>();
        private double discountPct;
        public void add(Item item) { items.add(item); }       // mutates the cart
        public void applyDiscount(double pct) { discountPct = pct; }
        public double total() {
            double subtotal = 0;
            for (Item i : items) subtotal += i.price() * i.qty();
            return round2(subtotal * (1 - discountPct / 100));
        }
    }

    static double round2(double x) { return Math.round(x * 100) / 100.0; }

    public static double lineTotal(Item i) { return i.price() * i.qty(); }      // FP: small pure functions
    public static DoubleUnaryOperator discount(double pct) {                    // higher-order: returns a function
        return amount -> amount * (1 - pct / 100);
    }
    public static double cartTotal(List<Item> items, double pct) {
        double subtotal = items.stream().mapToDouble(OopVsFp::lineTotal).sum();
        return discount(pct).andThen(OopVsFp::round2).applyAsDouble(subtotal);   // composition
    }

    // =============================== 2. Shapes =========================================
    public abstract static class ShapeOop { public abstract double area(); }     // OOP: open to new TYPES
    public static final class CircleOop extends ShapeOop {
        private final double r;
        public CircleOop(double r) { this.r = r; }
        public double area() { return Math.PI * r * r; }
    }
    public static final class SquareOop extends ShapeOop {
        private final double s;
        public SquareOop(double s) { this.s = s; }
        public double area() { return s * s; }
    }

    public sealed interface Shape permits Circle, Square {}                        // FP: plain data...
    public record Circle(double r) implements Shape {}
    public record Square(double s) implements Shape {}

    public static double area(Shape shape) {                                       // ...+ functions: open to new OPERATIONS
        return switch (shape) {
            case Circle(var r) -> Math.PI * r * r;
            case Square(var s) -> s * s;
        };
    }
    public static double perimeter(Shape shape) {                                  // a new operation = one new function
        return switch (shape) {
            case Circle(var r) -> 2 * Math.PI * r;
            case Square(var s) -> 4 * s;
        };
    }

    // =============================== 3. Failure handling ===============================
    public static final class OrderException extends RuntimeException {
        public OrderException(String m) { super(m); }
    }

    public static final class OrderProcessor {                                      // OOP + exceptions + mutation
        private final Map<String, Integer> stock;
        public OrderProcessor(Map<String, Integer> stock) { this.stock = new HashMap<>(stock); }
        public int place(String name, int qty) {
            if (qty <= 0) throw new OrderException("qty must be positive");
            if (stock.getOrDefault(name, 0) < qty) throw new OrderException("out of stock");
            return stock.merge(name, -qty, Integer::sum);                           // side effect
        }
    }

    /** A Result type built from a sealed interface + records. */
    public sealed interface Result<T> permits Ok, Err {
        <U> Result<U> map(Function<? super T, ? extends U> fn);
        <U> Result<U> flatMap(Function<? super T, Result<U>> fn);
    }
    public record Ok<T>(T value) implements Result<T> {
        public <U> Result<U> map(Function<? super T, ? extends U> fn) { return new Ok<>(fn.apply(value)); }
        public <U> Result<U> flatMap(Function<? super T, Result<U>> fn) { return fn.apply(value); }
    }
    public record Err<T>(String error) implements Result<T> {
        public <U> Result<U> map(Function<? super T, ? extends U> fn) { return new Err<>(error); }   // short-circuit
        public <U> Result<U> flatMap(Function<? super T, Result<U>> fn) { return new Err<>(error); }
    }

    public record Order(String name, int qty) {}

    static Result<Order> validateQty(Order o) {
        return o.qty() > 0 ? new Ok<>(o) : new Err<>("qty must be positive");
    }
    static Function<Order, Result<Order>> checkStock(Map<String, Integer> stock) {
        return o -> stock.getOrDefault(o.name(), 0) >= o.qty() ? new Ok<>(o) : new Err<>("out of stock");
    }

    /** Pure: returns a NEW stock map (or an Err); the input is never mutated. */
    public static Result<Map<String, Integer>> placeOrder(Map<String, Integer> stock, String name, int qty) {
        return new Ok<>(new Order(name, qty))
                .flatMap(OopVsFp::validateQty)
                .flatMap(checkStock(stock))
                .map(o -> {
                    var copy = new HashMap<>(stock);
                    copy.merge(o.name(), -o.qty(), Integer::sum);
                    return Map.copyOf(copy);
                });
    }

    // =============================== 4. Bank account ===================================
    public static final class Account {                                               // OOP: live entity
        private double balance;
        public void deposit(double amt) { balance += amt; }
        public void withdraw(double amt) {
            if (amt > balance) throw new IllegalArgumentException("insufficient funds");
            balance -= amt;
        }
        public double balance() { return balance; }
    }

    public sealed interface Event permits Deposited, Withdrew {}                      // FP: state = fold(events)
    public record Deposited(double amount) implements Event {}
    public record Withdrew(double amount) implements Event {}

    public static double apply(double balance, Event e) {
        return switch (e) {
            case Deposited(var a) -> balance + a;
            case Withdrew(var a) -> balance - a;
        };
    }
    public static double balanceFromEvents(List<Event> events) {
        double b = 0;
        for (Event e : events) b = apply(b, e);       // a left fold; streams' reduce needs an associative op
        return b;
    }

    // =============================== 5. Strategy =========================================
    public interface PricingStrategy { double price(double base); }
    public static final class BlackFriday implements PricingStrategy {
        public double price(double base) { return base * 0.5; }
    }
    public static double checkoutOop(double base, PricingStrategy s) { return s.price(base); }
    public static double checkoutFp(double base, DoubleUnaryOperator pricing) { return pricing.applyAsDouble(base); }

    // =============================== 6. Parallel word count ============================
    public static final class SharedCounter {                                         // shared mutable state needs a lock
        private final Map<String, Long> counts = new HashMap<>();
        private final ReentrantLock lock = new ReentrantLock();
        public void addText(String text) {
            lock.lock();
            try {
                for (String w : text.split("\\s+")) if (!w.isEmpty()) counts.merge(w, 1L, Long::sum);
            } finally {
                lock.unlock();
            }
        }
        public Map<String, Long> counts() { return Map.copyOf(counts); }
    }

    /** FP: no shared state, no locks; the stream merges per-thread results for us. */
    public static Map<String, Long> parallelCount(List<String> chunks) {
        return chunks.parallelStream()
                .flatMap(c -> Stream.of(c.split("\\s+")))
                .filter(w -> !w.isEmpty())
                .collect(Collectors.groupingByConcurrent(w -> w, Collectors.counting()));
    }

    // ---- hybrid: immutable object with a "functional" update ------------------------
    public record Settings(String theme, int fontSize) {
        public Settings() { this("light", 14); }
        public Settings withTheme(String t) { return new Settings(t, fontSize); }
    }
}
