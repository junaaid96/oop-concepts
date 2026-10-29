package oop.dbc;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import oop.dbc.Dbc.*;
import org.junit.jupiter.api.Test;

class DbcTest {
    @Test
    void preconditions() {
        var a = new Account();
        a.deposit(10);
        var e = assertThrows(ContractViolation.class, () -> a.withdraw(20));
        assertTrue(e.getMessage().startsWith("precondition failed"));
        assertThrows(ContractViolation.class, () -> a.deposit(0));
        a.withdraw(10);
        assertEquals(0, a.balance());
    }

    @Test
    void invariantSorted() {
        var s = new SortedList();
        for (int x : new int[] {5, 1, 3}) s.insert(x);
        assertEquals(List.of(1, 3, 5), s.items());
    }

    @Test
    void jdkHelpers() {
        assertThrows(NullPointerException.class, () -> Dbc.greet(null));
        assertThrows(IndexOutOfBoundsException.class, () -> Dbc.greet(""));
        assertEquals("hello Ana", Dbc.greet("Ana"));
    }

    @Test
    void assertionsEnabledInTests() {
        assertTrue(Dbc.class.desiredAssertionStatus());
    }
}
