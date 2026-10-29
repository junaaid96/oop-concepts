package oop.equality;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oop.equality.Equality.*;
import org.junit.jupiter.api.Test;

class EqualityTest {
    @Test
    void entity() {
        var a = new Customer(1, "Ana");
        var b = new Customer(1, "Ana Maria");
        assertEquals(a, b);
        assertNotSame(a, b);
        assertEquals(1, new HashSet<>(List.of(a, b)).size());
    }

    @Test
    void valueObject() {
        assertEquals(new Color(1, 2, 3), new Color(1, 2, 3));
        assertTrue(Set.of(new Color(1, 2, 3)).contains(new Color(1, 2, 3)));
    }

    @Test
    void brokenHashCodeBreaksHashSet() {
        var set = new HashSet<BrokenKey>();
        set.add(new BrokenKey("k"));
        assertEquals(new BrokenKey("k"), new BrokenKey("k"));
        assertFalse(set.contains(new BrokenKey("k")));   // equal, but lands in another bucket
    }

    @Test
    void stringIdentityVsEquality() {
        String a = "hi";
        String b = new String("hi");
        assertNotSame(a, b);
        assertEquals(a, b);
        assertSame(a, b.intern());
        assertTrue(Equality.sameText(null, null));
    }

    @Test
    void copies() {
        var r = Equality.aliasingDemo();
        assertEquals(List.of(2, 3, 4), r.get(0));
        assertEquals(List.of(2, 3, 4), r.get(1));
        assertEquals(List.of(2, 3), r.get(2));
    }
}
