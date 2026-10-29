package oop.nested;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import oop.nested.Nested.*;
import org.junit.jupiter.api.Test;

class NestedTest {
    @Test
    void nestedAndInnerIterator() {
        var list = new LinkedList<Integer>();
        list.pushFront(1);
        list.pushFront(2);
        List<Integer> out = new ArrayList<>();
        for (int v : list) out.add(v);
        assertEquals(List.of(2, 1), out);
        assertEquals(2, list.size());
    }

    @Test
    void innerMutatesOuter() {
        var b = new Button();
        var c = b.clicker();
        c.click();
        b.new Clicker().click();
        assertEquals(2, b.clicks());
    }

    @Test
    void localAnonymousLambda() {
        var v = Nested.makeValidator(3);
        assertTrue(v.test("abc"));
        assertFalse(v.test("ab"));
        var g = Nested.anonymousGreeter();
        g.greet("a");
        assertEquals("hi b #2", g.greet("b"));
        assertEquals("hi c", Nested.lambdaGreeter().greet("c"));
        var counter = Nested.makeCounter();
        counter.getAsInt();
        assertEquals(2, counter.getAsInt());
    }
}
