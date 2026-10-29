package oop.modeling;

/** Enums, records and sealed sum types with exhaustive pattern matching. */
public final class Modeling {
    private Modeling() {}

    /** Enums are full classes: fields, constructors and behaviour. */
    public enum OrderStatus {
        PENDING("pending"), PAID("paid"), SHIPPED("shipped"), CANCELLED("cancelled");

        private final String code;
        OrderStatus(String code) { this.code = code; }

        public String code() { return code; }
        public boolean isFinal() { return this == SHIPPED || this == CANCELLED; }

        public static OrderStatus fromCode(String code) {
            for (var s : values()) if (s.code.equals(code)) return s;
            throw new IllegalArgumentException("unknown status " + code);
        }
    }

    /** A record: immutable, equals/hashCode/toString generated. */
    public record Point(double x, double y) {}

    /** Sealed interface: the compiler knows every variant. */
    public sealed interface Shape permits Circle, Square {}
    public record Circle(double radius) implements Shape {}
    public record Square(double side) implements Shape {}

    /** Exhaustive switch with record patterns: no default needed; a new variant is a compile error. */
    public static double area(Shape shape) {
        return switch (shape) {
            case Circle(double r) -> Math.PI * r * r;
            case Square(double s) -> s * s;
        };
    }

    /** Record patterns can nest and use guards. */
    public static String quadrant(Object o) {
        return switch (o) {
            case Point(double x, double y) when x == 0 && y == 0 -> "origin";
            case Point(double x, _) when x > 0 -> "right";          // unnamed pattern "_" (Java 22+)
            case Point _ -> "left";
            default -> "not a point";
        };
    }
}
