package oop.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** The most-used GoF patterns in idiomatic modern Java. */
public final class Patterns {
    private Patterns() {}

    // ---- Creational ---------------------------------------------------------------
    /** Singleton via enum: thread-safe, serialization-safe. Prefer DI in real apps. */
    public enum Config {
        INSTANCE;
        private final Map<String, String> values = Map.of("env", "dev");
        public String get(String key) { return values.get(key); }
    }

    /** Factory: callers ask for a kind, not a concrete class. */
    public sealed interface Notification permits Email, Sms {}
    public record Email(String to) implements Notification {}
    public record Sms(String to) implements Notification {}

    public static Notification create(String kind, String to) {
        return switch (kind) {
            case "email" -> new Email(to);
            case "sms" -> new Sms(to);
            default -> throw new IllegalArgumentException("unknown kind " + kind);
        };
    }

    /** Builder: readable construction of an object with many optional parts. */
    public record Pizza(String size, List<String> toppings, boolean extraCheese) {
        public static Builder builder(String size) { return new Builder(size); }

        public static final class Builder {
            private final String size;
            private final List<String> toppings = new ArrayList<>();
            private boolean extraCheese;
            private Builder(String size) { this.size = size; }
            public Builder topping(String t) { toppings.add(t); return this; }
            public Builder extraCheese() { extraCheese = true; return this; }
            public Pizza build() { return new Pizza(size, List.copyOf(toppings), extraCheese); }
        }
    }

    // ---- Structural ---------------------------------------------------------------
    public interface Coffee { double cost(); String description(); }
    public record Espresso() implements Coffee {
        public double cost() { return 2; }
        public String description() { return "espresso"; }
    }
    /** Decorator: wraps an object with the same interface and adds behaviour. */
    public record WithMilk(Coffee inner) implements Coffee {
        public double cost() { return inner.cost() + 0.5; }
        public String description() { return inner.description() + " + milk"; }
    }

    /** Adapter: makes an incompatible API fit the interface we expect. */
    public static final class LegacyThermometer { public double fahrenheit() { return 212; } }
    public interface CelsiusSensor { double celsius(); }
    public record ThermometerAdapter(LegacyThermometer legacy) implements CelsiusSensor {
        public double celsius() { return (legacy.fahrenheit() - 32) * 5 / 9; }
    }

    // ---- Behavioural ----------------------------------------------------------------
    /** Strategy: swap an algorithm. With a functional interface, a lambda IS a strategy. */
    @FunctionalInterface public interface ShippingStrategy { double cost(double kg); }
    public static final ShippingStrategy STANDARD = kg -> 5 + kg;

    public static double shippingCost(ShippingStrategy s, double kg) { return s.cost(kg); }

    /** Observer: subscribers are notified when an event is published. */
    public static final class EventBus<E> {
        private final List<Consumer<? super E>> subscribers = new ArrayList<>();
        public Runnable subscribe(Consumer<? super E> s) {
            subscribers.add(s);
            return () -> subscribers.remove(s);          // unsubscribe handle
        }
        public void publish(E event) { subscribers.forEach(s -> s.accept(event)); }
    }

    /** State with an enum: each constant knows its successor. */
    public enum Light {
        RED { public Light next() { return GREEN; } },
        GREEN { public Light next() { return YELLOW; } },
        YELLOW { public Light next() { return RED; } };
        public abstract Light next();
    }

    /** Command: requests as objects, enabling undo. */
    public interface Command { void execute(); void undo(); }
    public static final class TextEditor {
        private final StringBuilder text = new StringBuilder();
        private final List<Command> history = new ArrayList<>();

        public void type(String s) {
            Command c = new Command() {
                public void execute() { text.append(s); }
                public void undo() { text.setLength(text.length() - s.length()); }
            };
            c.execute();
            history.add(c);
        }
        public void undo() { if (!history.isEmpty()) history.removeLast().undo(); }
        public String text() { return text.toString(); }
    }

    /** Template method via a Supplier hook (functional variant). */
    public static String report(Supplier<String> body) { return "== header ==\n" + body.get() + "\n== footer =="; }
}
