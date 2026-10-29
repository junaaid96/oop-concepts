package oop.antipatterns;

import java.util.ArrayList;
import java.util.List;

/** Common OOP anti-patterns next to their fixes. */
public final class AntiPatterns {
    private AntiPatterns() {}

    // ---- Anemic domain model (anti-pattern) ---------------------------------------
    /** Just getters/setters: any service can put it into an invalid state. */
    public static final class AnemicOrder {
        private final List<Double> items = new ArrayList<>();
        private String status = "new";
        public List<Double> getItems() { return items; }        // leaks the mutable list
        public String getStatus() { return status; }
        public void setStatus(String s) { status = s; }
    }

    public static void payAnemic(AnemicOrder order) { order.setStatus("paid"); }   // nothing stops paying an empty order

    // ---- Rich domain model (fix) -----------------------------------------------------
    public enum Status { NEW, PAID }

    public static final class RichOrder {
        private final List<Double> items = new ArrayList<>();
        private Status status = Status.NEW;

        public Status status() { return status; }

        public void add(double price) {
            if (status != Status.NEW) throw new IllegalStateException("cannot modify a paid order");
            items.add(price);
        }

        public void pay() {                                      // behaviour lives with the data it protects
            if (items.isEmpty()) throw new IllegalStateException("cannot pay an empty order");
            if (status != Status.NEW) throw new IllegalStateException("already paid");
            status = Status.PAID;
        }

        public double total() { return items.stream().mapToDouble(Double::doubleValue).sum(); }
        public List<Double> items() { return List.copyOf(items); }   // defensive copy
    }

    // ---- Deep inheritance (anti-pattern) vs composition (fix) ------------------------
    public static class Animal { public String sound() { return "..."; } }
    public static class Mammal extends Animal {}
    public static class Canine extends Mammal {}
    public static class Dog extends Canine {}
    public static final class RobotDog extends Dog {             // a robot is not an animal
        @Override public String sound() { return "beep-woof"; }
    }

    public record Creature(String name, String sound, boolean canWalk) {   // flat: assembled from parts
        public String speak() { return name + ": " + sound; }
    }

    // ---- God object (anti-pattern) vs split responsibilities (fix) -------------------
    /** Validates, stores, emails and reports: four reasons to change. */
    public static final class GodApp {
        public final List<String> users = new ArrayList<>();
        public final List<String> outbox = new ArrayList<>();
        public void register(String email) {
            if (!email.contains("@")) throw new IllegalArgumentException();
            users.add(email);
            outbox.add("welcome " + email);
        }
        public String report() { return users.size() + " users"; }
    }

    public static final class EmailValidator {
        public void check(String email) {
            if (!email.contains("@")) throw new IllegalArgumentException("invalid email");
        }
    }

    public static final class UserStore {
        private final List<String> users = new ArrayList<>();
        public void add(String email) { users.add(email); }
        public List<String> users() { return List.copyOf(users); }
    }

    public static final class Mailer {
        private final List<String> outbox = new ArrayList<>();
        public void welcome(String email) { outbox.add("welcome " + email); }
        public List<String> outbox() { return List.copyOf(outbox); }
    }

    /** Thin coordinator. */
    public record Registration(EmailValidator validator, UserStore store, Mailer mailer) {
        public void register(String email) {
            validator.check(email);
            store.add(email);
            mailer.welcome(email);
        }
    }

    // ---- Primitive obsession (anti-pattern) vs value object (fix) --------------------
    public static double priceInCents(double amount) { return amount * 100; }   // which currency? negative?

    public record Money(long cents, String currency) {
        public Money {
            if (cents < 0) throw new IllegalArgumentException("negative money");
            if (currency.length() != 3) throw new IllegalArgumentException("ISO currency code");
        }
    }
}
