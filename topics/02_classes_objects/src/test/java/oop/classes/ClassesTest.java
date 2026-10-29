package oop.classes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import oop.classes.Classes.*;
import org.junit.jupiter.api.Test;

class ClassesTest {
    @Test
    void constructorAndStatic() {
        int before = User.count();
        var u = User.fromCsv("Bo, bo@example.com");
        assertEquals("Bo", u.name());
        assertEquals("bo@example.com", u.email());
        assertEquals(before + 1, User.count());
        assertThrows(IllegalArgumentException.class, () -> new User("x", "no-at-sign"));
        assertTrue(User.isValidName("Ana"));
        assertFalse(User.isValidName("An4"));
        assertThrows(IllegalArgumentException.class, () -> new AdminUser("Eve", "eve@example.com"));
        assertEquals("Root", new AdminUser("Root", "root@admin.example").name());
    }

    @Test
    void immutableRecord() {
        var m = new Money(10);
        assertEquals(new Money(15), m.add(new Money(5)));
        assertEquals("EUR", m.withCurrency("EUR").currency());
        assertEquals("USD", m.currency());                 // original unchanged
        assertEquals(1, Set.of(new Money(1)).size());
        assertEquals(new Money(1).hashCode(), new Money(1).hashCode());
        assertThrows(IllegalArgumentException.class, () -> new Money(-1));
    }

    @Test
    void computedProperty() {
        assertEquals(212, new Temperature(100).fahrenheit());
    }
}
