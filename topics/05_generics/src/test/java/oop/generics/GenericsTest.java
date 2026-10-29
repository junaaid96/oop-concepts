package oop.generics;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import oop.generics.Generics.*;
import org.junit.jupiter.api.Test;

class GenericsTest {
    @Test
    void boxMap() {
        assertEquals(new Box<>("42"), new Box<>(21).map(x -> x * 2).map(String::valueOf));
    }

    @Test
    void genericMethods() {
        assertEquals(Optional.of("a"), Generics.first(List.of("a", "b")));
        assertTrue(Generics.first(List.of()).isEmpty());
        assertEquals(1, Generics.smallest(List.of(3, 1, 2)));
        assertEquals("a", Generics.smallest(List.of("b", "a")));
    }

    @Test
    void wildcards() {
        assertEquals(3.5, Generics.sum(List.of(1, 2.5)));
        List<Number> sink = new ArrayList<>();
        Generics.addInts(sink, 3);
        assertEquals(List.of(0, 1, 2), sink);
    }

    @Test
    void repository() {
        var repo = new Repository<Integer, String>();
        repo.add(1, "Ana");
        assertEquals(Optional.of("Ana"), repo.get(1));
        assertTrue(repo.get(2).isEmpty());
    }
}
