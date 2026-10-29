package oop.composition;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/** Composition and delegation instead of inheritance. */
public final class Composition {
    private Composition() {}

    public interface Engine { String start(); }
    public static final class PetrolEngine implements Engine { public String start() { return "vroom"; } }
    public static final class ElectricEngine implements Engine { public String start() { return "hum"; } }

    /** A Car HAS an engine (composition). The engine can be swapped at runtime. */
    public static final class Car {
        private Engine engine;
        public Car(Engine engine) { this.engine = engine; }      // the part is injected, not hard-wired
        public void setEngine(Engine engine) { this.engine = engine; }
        public String start() { return "car: " + engine.start(); }   // delegation
    }

    /** Inheritance misuse: java.util.Stack extends Vector, so add(0, x), sort(...) etc. break LIFO. */
    public static final class BadStack<T> extends ArrayList<T> {}

    /** Composition: wraps a Deque and exposes only stack operations. */
    public static final class Stack<T> {
        private final Deque<T> items = new ArrayDeque<>();
        public void push(T x) { items.push(x); }
        public T pop() { return items.pop(); }
        public int size() { return items.size(); }
    }

    /** Delegation with a wrapper: read operations forward, mutation is blocked. */
    public static <T> List<T> readOnlyView(List<T> target) {
        return Collections.unmodifiableList(target);   // JDK decorator: delegates reads, throws on writes
    }

    /** Hand-written forwarding decorator: adds logging, delegates the rest. */
    public static final class LoggingList<T> {
        private final List<T> items = new ArrayList<>();
        private final List<String> log = new ArrayList<>();
        public void add(T x) { log.add("add " + x); items.add(x); }
        public T get(int i) { return items.get(i); }
        public int size() { return items.size(); }
        public List<String> log() { return List.copyOf(log); }
    }

    public static void main(String[] args) {
        var car = new Car(new PetrolEngine());
        System.out.println(car.start());
        car.setEngine(new ElectricEngine());
        System.out.println(car.start());
    }
}
