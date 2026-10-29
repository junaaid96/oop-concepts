package oop.contracts;

import java.util.List;
import java.util.stream.Collectors;

/** Interfaces vs abstract classes. */
public final class Contracts {
    private Contracts() {}

    /** Interface: a capability/contract. No instance state; may have default and static methods. */
    @FunctionalInterface
    public interface Exporter {
        String export(List<List<Object>> rows);

        default String exportOne(List<Object> row) { return export(List.of(row)); }   // default method
    }

    public static final class JsonExporter implements Exporter {
        @Override
        public String export(List<List<Object>> rows) {
            return rows.stream()
                    .map(r -> r.stream().map(Contracts::json).collect(Collectors.joining(", ", "[", "]")))
                    .collect(Collectors.joining(", ", "[", "]"));
        }
    }

    private static String json(Object v) {
        return v instanceof String s ? "\"" + s + "\"" : String.valueOf(v);
    }

    /** Abstract class: shared state + shared code + abstract hooks. */
    public abstract static class Vehicle {
        protected final String brand;

        protected Vehicle(String brand) { this.brand = brand; }   // abstract classes CAN have constructors

        public abstract int wheels();                             // hook every subclass must provide

        public final String summary() {                           // template method: final algorithm
            return brand + " with " + wheels() + " wheels";
        }
    }

    public static final class Car extends Vehicle {
        public Car(String brand) { super(brand); }
        @Override public int wheels() { return 4; }
    }

    public static final class Bike extends Vehicle {
        public Bike(String brand) { super(brand); }
        @Override public int wheels() { return 2; }
    }

    /** Java 8+ default and static methods, Java 9+ private interface methods. */
    public interface Greeter {
        String name();
        default String greet() { return decorate("Hello, " + name()); }   // default: inherited behaviour
        static Greeter of(String n) { return () -> n; }                    // static factory on the interface
        private String decorate(String s) { return s + "!"; }              // private helper, not part of the API
    }

    public interface Walker { default String move() { return "walk"; } }
    public interface Swimmer { default String move() { return "swim"; } }

    /** Diamond of defaults: the compiler forces us to resolve the conflict explicitly. */
    public static final class Duck implements Walker, Swimmer {
        @Override public String move() { return Walker.super.move() + "+" + Swimmer.super.move(); }
    }

    public static String saveReport(Exporter exporter, List<List<Object>> rows) {
        return exporter.export(rows);                             // depends on the interface only
    }

    public static void main(String[] args) {
        System.out.println(saveReport(new JsonExporter(), List.of(List.of(1, "a"))));
        Exporter csv = rows -> rows.toString();                   // a lambda implements a functional interface
        System.out.println(csv.exportOne(List.of(1, 2)));
        System.out.println(new Car("Volvo").summary());
    }
}
