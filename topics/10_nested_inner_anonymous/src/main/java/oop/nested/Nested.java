package oop.nested;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

/** Static nested, inner, local and anonymous classes, plus lambdas and closures. */
public final class Nested {
    private Nested() {}

    /** Singly linked list whose Node is a STATIC nested class and whose iterator is an INNER class. */
    public static final class LinkedList<T> implements Iterable<T> {
        private static final class Node<T> {          // static nested: no reference to the outer list
            final T value;
            final Node<T> next;
            Node(T value, Node<T> next) { this.value = value; this.next = next; }
        }

        private Node<T> head;
        private int size;

        public void pushFront(T value) { head = new Node<>(value, head); size++; }
        public int size() { return size; }

        @Override public Iterator<T> iterator() { return new Itr(); }

        private final class Itr implements Iterator<T> {   // inner: implicitly bound to LinkedList.this
            private Node<T> cur = head;                    // reads the outer instance's field
            @Override public boolean hasNext() { return cur != null; }
            @Override public T next() {
                if (cur == null) throw new NoSuchElementException();
                T v = cur.value;
                cur = cur.next;
                return v;
            }
        }
    }

    /** Inner class that mutates its outer instance. */
    public static final class Button {
        private int clicks;
        public final class Clicker {
            public void click() { clicks++; }              // same as Button.this.clicks++
        }
        public Clicker clicker() { return new Clicker(); }  // outside: button.new Clicker()
        public int clicks() { return clicks; }
    }

    /** Local class: visible only inside this method, captures effectively-final locals. */
    public static Predicate<String> makeValidator(int minLen) {
        final class LengthRule implements Predicate<String> {
            @Override public boolean test(String s) { return s.length() >= minLen; }
        }
        return new LengthRule();
    }

    public interface Greeter { String greet(String name); }

    /** Anonymous class: a one-off implementation, can hold state. */
    public static Greeter anonymousGreeter() {
        return new Greeter() {
            private int calls;
            @Override public String greet(String n) { calls++; return "hi " + n + " #" + calls; }
        };
    }

    /** Lambda: the concise replacement when the interface has ONE abstract method. */
    public static Greeter lambdaGreeter() { return n -> "hi " + n; }

    /** Closure over mutable state: captured variables must be effectively final, so use a holder. */
    public static IntSupplier makeCounter() {
        int[] n = {0};
        return () -> ++n[0];
    }
}
