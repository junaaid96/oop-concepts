package oop.contracts;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import oop.contracts.Contracts.*;
import org.junit.jupiter.api.Test;

class ContractsTest {
    @Test
    void interfaceImplementations() {
        Exporter json = new JsonExporter();
        assertEquals("[[1, \"a\"]]", Contracts.saveReport(json, List.of(List.of(1, "a"))));
        Exporter lambda = rows -> "rows=" + rows.size();
        assertEquals("rows=1", lambda.exportOne(List.of(1)));
    }

    @Test
    void abstractClassTemplateMethod() {
        assertEquals("Volvo with 4 wheels", new Car("Volvo").summary());
        assertEquals("BMX with 2 wheels", new Bike("BMX").summary());
        Vehicle v = new Bike("x");
        assertInstanceOf(Vehicle.class, v);
    }

    @Test
    void interfaceMethodKinds() {
        assertEquals("Hello, Ana!", Greeter.of("Ana").greet());
        assertEquals("walk+swim", new Duck().move());
    }
}
