package oop.basics;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BasicsTest {
    @Test
    void everyDemoRuns() {
        assertDoesNotThrow(() -> {
            ClassDemo.main(new String[0]);
            InheritanceDemo.main(new String[0]);
            EncapsulationDemo.main(new String[0]);
            PolymorphismDemo.main(new String[0]);
            AbstractionDemo.main(new String[0]);
        });
    }

    @Test
    void behaviour() {
        assertEquals("Derived display", ((PolymorphismDemo.Base) new PolymorphismDemo.Derived()).display());
        assertEquals(6, PolymorphismDemo.addAll(1, 2, 3));
        var bat = new InheritanceDemo.Bat();
        assertEquals("Laying eggs", bat.layEggs());
        assertThrows(IllegalArgumentException.class, () -> new EncapsulationDemo.MyClass().setVar(-1));
    }
}
