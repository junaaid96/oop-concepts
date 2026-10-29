package oop.pillars;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The four pillars of OOP (encapsulation, abstraction, inheritance, polymorphism) in modern Java.
 *
 * <p>Run: {@code java topics/01_four_pillars/src/main/java/oop/pillars/Pillars.java}
 * <br>Test: {@code ./mvnw -pl topics/01_four_pillars test}
 */
public final class Pillars {
    private Pillars() {}

    // ------------------------------------------------------------ Encapsulation
    /** Keeps its balance private and only changes it through validated behaviour. */
    public static class BankAccount {
        private final String owner;     // final: set once in the constructor
        protected double balance;       // protected: visible to subclasses, hidden from clients

        public BankAccount(String owner) { this.owner = owner; }

        public String owner() { return owner; }
        public double balance() { return balance; }   // getter only: no setBalance() exists

        public void deposit(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("deposit must be positive");
            balance += amount;          // invariant "deposits are positive" is guarded above
        }

        public void withdraw(double amount) {
            if (amount > balance) throw new IllegalArgumentException("insufficient funds");
            balance -= amount;          // invariant "balance never negative"
        }
    }

    // ---------------------------------------- Abstraction + subtype polymorphism
    /** Abstract base: clients only need to know a shape has an area. */
    public abstract static class Shape {
        public abstract double area();              // subclasses MUST implement this

        public String describe() {                  // template method reusing the abstract one
            return "%s with area %.2f".formatted(getClass().getSimpleName(), area());
        }
    }

    public static final class Circle extends Shape {   // inheritance: a Circle *is a* Shape
        private final double r;
        public Circle(double r) { this.r = r; }
        @Override public double area() { return Math.PI * r * r; }   // dynamic dispatch
    }

    public static final class Rectangle extends Shape {
        private final double w, h;
        public Rectangle(double w, double h) { this.w = w; this.h = h; }
        @Override public double area() { return w * h; }
    }

    /** Works with ANY current or future Shape: the method is bound at runtime. */
    public static double totalArea(List<? extends Shape> shapes) {
        return shapes.stream().mapToDouble(Shape::area).sum();
    }

    // ------------------------------------------------------ Ad hoc polymorphism
    /** Java has no operator overloading: "+" becomes a well-named method; equals() is generated. */
    public record Vector2(double x, double y) {
        public Vector2 plus(Vector2 o) { return new Vector2(x + o.x, y + o.y); }
    }

    // Method OVERLOADING: the compiler picks the version from the static argument types.
    public static String toText(double value) { return "%.2f".formatted(value); }
    public static String toText(String value) { return value; }
    public static String toText(List<?> values) {
        return values.stream().map(Pillars::toTextAny).collect(Collectors.joining(", "));
    }
    /** Runtime choice by type with a pattern-matching switch (Java 21+). */
    public static String toTextAny(Object value) {
        return switch (value) {
            case Double d -> toText(d);
            case String s -> toText(s);
            case List<?> l -> toText(l);
            default -> String.valueOf(value);
        };
    }

    // --------------------------------------------------- Parametric polymorphism
    /** One implementation, any element type. */
    public static final class Stack<T> {
        private final Deque<T> items = new ArrayDeque<>();
        public void push(T item) { items.push(item); }
        public T pop() { return items.pop(); }
        public int size() { return items.size(); }
    }

    // --------------------------------------------- Interface-based polymorphism
    /** Java is nominal: a type must declare {@code implements Speaker} to be one. */
    public interface Speaker { String speak(); }
    public static final class Duck implements Speaker { public String speak() { return "Quack"; } }
    public static final class Robot implements Speaker { public String speak() { return "Beep"; } }

    public static String chorus(List<? extends Speaker> speakers) {
        return speakers.stream().map(Speaker::speak).collect(Collectors.joining(" "));
    }

    public static void main(String[] args) {
        var acc = new BankAccount("Ana");
        acc.deposit(100);
        acc.withdraw(30);
        System.out.println("balance: " + acc.balance());
        System.out.println(totalArea(List.of(new Circle(1), new Rectangle(2, 3))));
        System.out.println(new Vector2(1, 2).plus(new Vector2(3, 4)));
        System.out.println(toText(List.of(1.5, 2, "x")));
        var s = new Stack<Integer>();
        s.push(1);
        System.out.println("stack size: " + s.size());
        System.out.println(chorus(List.of(new Duck(), new Robot())));
    }
}
