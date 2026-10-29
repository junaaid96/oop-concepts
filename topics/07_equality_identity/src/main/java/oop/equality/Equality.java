package oop.equality;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Identity (==) vs equality (equals), value objects vs entities, and the hashCode contract. */
public final class Equality {
    private Equality() {}

    /** Entity: same id means same customer, even if the name changed. */
    public static final class Customer {
        private final long id;
        private String name;
        public Customer(long id, String name) { this.id = id; this.name = name; }
        public void rename(String n) { name = n; }
        public String name() { return name; }

        @Override public boolean equals(Object o) {
            return o instanceof Customer c && c.id == id;    // pattern matching for instanceof
        }
        @Override public int hashCode() { return Long.hashCode(id); }   // consistent with equals
    }

    /** Value object: equal when all components are equal. A record generates all of this. */
    public record Color(int r, int g, int b) {}

    /** BROKEN on purpose: overrides equals but not hashCode, so HashSet/HashMap misbehave. */
    public static final class BrokenKey {
        private final String key;
        public BrokenKey(String key) { this.key = key; }
        @Override public boolean equals(Object o) { return o instanceof BrokenKey k && k.key.equals(key); }
        // hashCode() NOT overridden -> identity hash, different for equal objects
    }

    /** Copying: alias shares, shallow copy shares the inner list, deep copy is independent. */
    public static List<List<Integer>> aliasingDemo() {
        List<List<Integer>> original = new ArrayList<>(List.of(new ArrayList<>(List.of(2, 3))));
        List<List<Integer>> alias = original;                      // same object
        List<List<Integer>> shallow = new ArrayList<>(original);   // new outer list, shared inner list
        List<List<Integer>> deep = new ArrayList<>();
        for (var inner : original) deep.add(new ArrayList<>(inner));
        alias.getFirst().add(4);                                   // visible via original, alias, shallow
        return List.of(original.getFirst(), shallow.getFirst(), deep.getFirst());
    }

    public static boolean sameText(String a, String b) {
        return Objects.equals(a, b);                               // null-safe equality
    }
}
