package oop.modeling;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import oop.modeling.Modeling.*;
import org.junit.jupiter.api.Test;

class ModelingTest {
    @Test
    void enumWithBehaviour() {
        assertEquals(OrderStatus.PAID, OrderStatus.fromCode("paid"));
        assertTrue(OrderStatus.SHIPPED.isFinal());
        assertFalse(OrderStatus.PENDING.isFinal());
        assertThrows(IllegalArgumentException.class, () -> OrderStatus.fromCode("lost"));
    }

    @Test
    void records() {
        assertEquals(new Point(1, 2), new Point(1, 2));
        assertEquals(1, Set.of(new Point(1, 2)).size());
        assertEquals("Point[x=1.0, y=2.0]", new Point(1, 2).toString());
    }

    @Test
    void sealedSumType() {
        assertEquals(Math.PI, Modeling.area(new Circle(1)), 1e-9);
        assertEquals(9, Modeling.area(new Square(3)));
        assertTrue(Shape.class.isSealed());
        assertEquals("origin", Modeling.quadrant(new Point(0, 0)));
        assertEquals("right", Modeling.quadrant(new Point(2, -1)));
        assertEquals("left", Modeling.quadrant(new Point(-2, 1)));
        assertEquals("not a point", Modeling.quadrant("x"));
    }
}
