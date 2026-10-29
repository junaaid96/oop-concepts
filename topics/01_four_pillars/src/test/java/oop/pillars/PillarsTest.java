package oop.pillars;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import oop.pillars.Pillars.*;
import org.junit.jupiter.api.Test;

class PillarsTest {
    @Test
    void encapsulationGuardsInvariants() {
        var acc = new BankAccount("Ana");
        acc.deposit(50);
        assertThrows(IllegalArgumentException.class, () -> acc.withdraw(100));
        assertThrows(IllegalArgumentException.class, () -> acc.deposit(-1));
        assertEquals(50, acc.balance());
    }

    @Test
    void dynamicPolymorphism() {
        assertEquals(Math.PI + 6, Pillars.totalArea(List.of(new Circle(1), new Rectangle(2, 3))), 1e-9);
        assertTrue(new Circle(1).describe().startsWith("Circle"));
    }

    @Test
    void adHocPolymorphism() {
        assertEquals(new Vector2(4, 6), new Vector2(1, 2).plus(new Vector2(3, 4)));
        assertEquals("1.50, x", Pillars.toText(List.of(1.5, "x")));
    }

    @Test
    void parametricAndInterfacePolymorphism() {
        var s = new Stack<String>();
        s.push("a");
        assertEquals("a", s.pop());
        assertEquals("Quack Beep", Pillars.chorus(List.of(new Duck(), new Robot())));
    }
}
