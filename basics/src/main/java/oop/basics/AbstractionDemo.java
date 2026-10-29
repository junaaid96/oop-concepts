package oop.basics;

/** 7. Abstraction: expose WHAT (area, perimeter), hide HOW. */
public class AbstractionDemo {
    abstract static class Shape {
        abstract double area();
        abstract double perimeter();
    }
    static class Circle extends Shape {
        private final double r;
        Circle(double r) { this.r = r; }
        double area() { return Math.PI * r * r; }
        double perimeter() { return 2 * Math.PI * r; }
    }
    static class Rectangle extends Shape {
        private final double l, w;
        Rectangle(double l, double w) { this.l = l; this.w = w; }
        double area() { return l * w; }
        double perimeter() { return 2 * (l + w); }
    }

    public static void main(String[] args) {
        Shape[] shapes = {new Circle(7), new Rectangle(5, 10)};
        for (Shape s : shapes)
            System.out.printf("%s area=%.2f perimeter=%.2f%n", s.getClass().getSimpleName(), s.area(), s.perimeter());
    }
}
