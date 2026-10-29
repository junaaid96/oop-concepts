package oop.composition;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import oop.composition.Composition.*;
import org.junit.jupiter.api.Test;

class CompositionTest {
    @Test
    void swapPartsAtRuntime() {
        var car = new Car(new PetrolEngine());
        assertEquals("car: vroom", car.start());
        car.setEngine(new ElectricEngine());
        assertEquals("car: hum", car.start());
    }

    @Test
    void compositionHidesUnwantedApi() {
        var bad = new BadStack<Integer>();
        bad.add(1);
        bad.add(0, 2);                          // leaky inheritance: insert at the bottom is allowed
        assertEquals(2, bad.getFirst());
        var s = new Stack<Integer>();
        s.push(1);
        s.push(2);
        assertEquals(2, s.pop());
        assertEquals(1, s.size());
    }

    @Test
    void delegation() {
        var view = Composition.readOnlyView(new ArrayList<>(List.of(3, 1, 2)));
        assertEquals(1, view.indexOf(1));
        assertThrows(UnsupportedOperationException.class, () -> view.add(4));
        var log = new LoggingList<String>();
        log.add("a");
        assertEquals(List.of("add a"), log.log());
    }
}
