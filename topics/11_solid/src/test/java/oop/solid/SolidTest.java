package oop.solid;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import oop.solid.Solid.*;
import org.junit.jupiter.api.Test;

class SolidTest {
    private final List<LineItem> items = List.of(new LineItem("pen", 2.5, 4), new LineItem("book", 20, 1));

    @Test
    void openClosedDiscounts() {
        assertEquals(30, new InvoiceCalculator(List.of()).total(items));
        assertEquals(25, new InvoiceCalculator(List.of(new FlatDiscount(5))).total(items));
        Discount tenPercent = t -> t * 0.9;                         // new rule, zero edits to the calculator
        assertEquals(27, new InvoiceCalculator(List.of(tenPercent)).total(items));
    }

    @Test
    void liskov() {
        List<Bird> birds = List.of(new Sparrow(), new Penguin());
        assertEquals(List.of("flies", "swims"), birds.stream().map(Bird::move).toList());
    }

    @Test
    void dependencyInversion() {
        var sender = new FakeSender();
        var service = new InvoiceService(new InvoiceCalculator(List.of()), new InvoiceFormatter(), sender);
        assertEquals(30, service.bill("ana@example.com", items));
        assertEquals(1, sender.sent().size());
        assertTrue(sender.sent().getFirst().body().endsWith("TOTAL 30.00"));
    }
}
