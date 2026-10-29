package oop.generics;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/** Generic classes, methods, bounds and wildcards in Java. */
public final class Generics {
    private Generics() {}

    /** A container that remembers the type of what it holds. */
    public record Box<T>(T value) {
        public <U> Box<U> map(Function<? super T, ? extends U> fn) {   // Box<Integer> -> Box<String>
            return new Box<>(fn.apply(value));
        }
    }

    /** The return type follows the input type; Optional instead of null. */
    public static <T> Optional<T> first(List<T> items) {
        return items.isEmpty() ? Optional.empty() : Optional.of(items.getFirst());
    }

    /** Bounded type parameter: only element types that can be compared. */
    public static <T extends Comparable<? super T>> T smallest(Collection<? extends T> items) {
        T result = null;
        for (T x : items) if (result == null || x.compareTo(result) < 0) result = x;
        if (result == null) throw new IllegalArgumentException("empty");
        return result;
    }

    /** PECS: Producer Extends, Consumer Super. */
    public static double sum(Collection<? extends Number> nums) {
        return nums.stream().mapToDouble(Number::doubleValue).sum();
    }
    public static void addInts(Collection<? super Integer> sink, int n) {
        for (int i = 0; i < n; i++) sink.add(i);
    }

    /** Generic in-memory repository: one implementation for Users, Orders, ... */
    public static final class Repository<ID, T> {
        private final Map<ID, T> items = new HashMap<>();
        public void add(ID id, T item) { items.put(id, item); }
        public Optional<T> get(ID id) { return Optional.ofNullable(items.get(id)); }
    }

    public static void main(String[] args) {
        System.out.println(new Box<>(21).map(x -> x * 2).map(String::valueOf));
        System.out.println(smallest(List.of("b", "a")));
    }
}
