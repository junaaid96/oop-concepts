package oop.antipatterns;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import oop.antipatterns.AntiPatterns.*;
import org.junit.jupiter.api.Test;

class AntiPatternsTest {
    @Test
    void anemicAllowsInvalidState() {
        var o = new AnemicOrder();
        AntiPatterns.payAnemic(o);
        assertEquals("paid", o.getStatus());
        o.getItems().add(9.0);                      // modified after payment!
        assertEquals(1, o.getItems().size());
    }

    @Test
    void richModelProtectsInvariants() {
        var o = new RichOrder();
        assertThrows(IllegalStateException.class, o::pay);
        o.add(5);
        o.pay();
        assertEquals(Status.PAID, o.status());
        assertThrows(IllegalStateException.class, () -> o.add(1));
        assertThrows(UnsupportedOperationException.class, () -> o.items().add(1.0));
        assertEquals(5, o.total());
    }

    @Test
    void compositionOverDeepHierarchy() {
        Animal a = new RobotDog();
        assertEquals("beep-woof", a.sound());
        assertEquals("rex: beep-woof", new Creature("rex", "beep-woof", true).speak());
    }

    @Test
    void godObjectSplit() {
        var god = new GodApp();
        god.register("a@x.io");
        assertEquals("1 users", god.report());
        var reg = new Registration(new EmailValidator(), new UserStore(), new Mailer());
        reg.register("b@x.io");
        assertEquals(List.of("b@x.io"), reg.store().users());
        assertEquals(List.of("welcome b@x.io"), reg.mailer().outbox());
        assertThrows(IllegalArgumentException.class, () -> reg.register("bad"));
    }

    @Test
    void valueObjectInsteadOfPrimitive() {
        assertEquals(-500, AntiPatterns.priceInCents(-5));
        assertThrows(IllegalArgumentException.class, () -> new Money(-1, "USD"));
        assertThrows(IllegalArgumentException.class, () -> new Money(1, "dollars"));
    }
}
