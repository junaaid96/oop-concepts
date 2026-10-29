package oop.classes;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/** Classes, objects, constructors, static members and immutability in modern Java. */
public final class Classes {
    private Classes() {}

    public static final int MAX_USERS = 100;           // compile-time constant

    public static class User {
        private static final AtomicInteger COUNT = new AtomicInteger();   // static: one per class
        private final String name;                                       // instance fields
        private final String email;

        public User(String name, String email) {
            if (!email.contains("@")) throw new IllegalArgumentException("invalid email");
            this.name = Objects.requireNonNull(name);
            this.email = email;
            COUNT.incrementAndGet();
        }

        /** Static factory: an "alternative constructor" with a descriptive name. */
        public static User fromCsv(String line) {
            String[] parts = line.split(",");
            return new User(parts[0].strip(), parts[1].strip());
        }

        public static boolean isValidName(String name) {      // utility: needs no instance
            return !name.isEmpty() && name.chars().allMatch(Character::isLetter);
        }

        public static int count() { return COUNT.get(); }
        public String name() { return name; }
        public String email() { return email; }
    }

    /** Subclass constructor: Java 25 (JEP 513) lets us validate BEFORE calling super(...). */
    public static final class AdminUser extends User {
        public AdminUser(String name, String email) {
            if (!email.endsWith("@admin.example")) throw new IllegalArgumentException("not an admin email");
            super(name, email);        // before Java 25 this had to be the first statement
        }
    }

    /** A record: immutable value object with generated constructor, accessors, equals/hashCode/toString. */
    public record Money(double amount, String currency) {
        public Money {                                         // compact constructor: validate
            if (amount < 0) throw new IllegalArgumentException("amount must be >= 0");
            Objects.requireNonNull(currency);
        }

        public Money(double amount) { this(amount, "USD"); }   // overloaded constructor = default arg

        public Money add(Money other) {                        // immutable: returns a NEW object
            if (!other.currency.equals(currency)) throw new IllegalArgumentException("currency mismatch");
            return new Money(amount + other.amount, currency);
        }

        public Money withCurrency(String c) { return new Money(amount, c); }   // a "wither"
    }

    public static final class Temperature {
        private final double celsius;
        public Temperature(double celsius) { this.celsius = celsius; }
        public double fahrenheit() { return celsius * 9 / 5 + 32; }   // computed property
    }

    public static void main(String[] args) {
        var u = User.fromCsv("Ana, ana@example.com");
        System.out.println(u.name() + " " + User.count() + " " + User.isValidName("Ana"));
        System.out.println(new Money(10).add(new Money(5)));
        System.out.println(new Temperature(100).fahrenheit());
    }
}
