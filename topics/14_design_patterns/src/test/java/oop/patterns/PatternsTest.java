package oop.patterns;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import oop.patterns.Patterns.*;
import org.junit.jupiter.api.Test;

class PatternsTest {
    @Test
    void creational() {
        assertSame(Config.INSTANCE, Config.valueOf("INSTANCE"));
        assertEquals("dev", Config.INSTANCE.get("env"));
        assertEquals(new Sms("1"), Patterns.create("sms", "1"));
        assertThrows(IllegalArgumentException.class, () -> Patterns.create("fax", "1"));
        var p = Pizza.builder("L").topping("olive").extraCheese().build();
        assertEquals(new Pizza("L", List.of("olive"), true), p);
    }

    @Test
    void structural() {
        Coffee c = new WithMilk(new WithMilk(new Espresso()));
        assertEquals(3, c.cost());
        assertEquals("espresso + milk + milk", c.description());
        assertEquals(100, new ThermometerAdapter(new LegacyThermometer()).celsius());
    }

    @Test
    void behavioural() {
        assertEquals(7, Patterns.shippingCost(Patterns.STANDARD, 2));
        assertEquals(21, Patterns.shippingCost(kg -> 15 + 3 * kg, 2));

        var bus = new EventBus<String>();
        var got = new ArrayList<String>();
        Runnable unsubscribe = bus.subscribe(got::add);
        bus.publish("a");
        unsubscribe.run();
        bus.publish("b");
        assertEquals(List.of("a"), got);

        assertEquals(Light.RED, Light.RED.next().next().next());

        var ed = new TextEditor();
        ed.type("hello");
        ed.type(" world");
        ed.undo();
        assertEquals("hello", ed.text());
        assertTrue(Patterns.report(() -> "x").contains("x"));
    }
}
