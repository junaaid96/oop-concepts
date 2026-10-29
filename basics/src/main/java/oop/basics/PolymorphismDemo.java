package oop.basics;

/** 6. Compile-time polymorphism (overloading, varargs) and run-time polymorphism (overriding). */
public class PolymorphismDemo {
    static int add(int a) { return a; }                    // overloads chosen by the compiler
    static int add(int a, int b) { return a + b; }
    static double add(double a, double b) { return a + b; }
    static int addAll(int... xs) { int s = 0; for (int x : xs) s += x; return s; }   // varargs

    static class Base { String display() { return "Base display"; } }
    static class Derived extends Base {
        @Override String display() { return "Derived display"; }   // chosen at run time
    }

    public static void main(String[] args) {
        System.out.println(add(1) + " " + add(1, 2) + " " + add(1.5, 2) + " " + addAll(1, 2, 3));
        Base b = new Derived();                // static type Base, dynamic type Derived
        System.out.println(b.display());       // "Derived display": every non-static, non-private method is virtual
    }
}
