package oop.oopvsfp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import oop.oopvsfp.OopVsFp.*;
import org.junit.jupiter.api.Test;

class OopVsFpTest {
    private final List<Item> items = List.of(new Item("a", 10, 2), new Item("b", 5, 1));

    @Test
    void cartBothWays() {
        var cart = new Cart();
        items.forEach(cart::add);
        cart.applyDiscount(10);
        assertEquals(22.5, cart.total());
        assertEquals(22.5, OopVsFp.cartTotal(items, 10));
    }

    @Test
    void shapesBothWays() {
        List<ShapeOop> oop = List.of(new CircleOop(1), new SquareOop(2));
        assertEquals(Math.PI + 4, oop.stream().mapToDouble(ShapeOop::area).sum(), 1e-9);
        assertEquals(Math.PI + 4, OopVsFp.area(new Circle(1)) + OopVsFp.area(new Square(2)), 1e-9);
        assertEquals(8, OopVsFp.perimeter(new Square(2)));
    }

    @Test
    void failureBothWays() {
        var p = new OrderProcessor(Map.of("pen", 3));
        assertEquals(1, p.place("pen", 2));
        assertThrows(OrderException.class, () -> p.place("pen", 5));

        var stock = Map.of("pen", 3);
        assertEquals(new Ok<>(Map.of("pen", 1)), OopVsFp.placeOrder(stock, "pen", 2));
        assertEquals(new Err<>("out of stock"), OopVsFp.placeOrder(stock, "pen", 9));
        assertEquals(new Err<>("qty must be positive"), OopVsFp.placeOrder(stock, "pen", 0));
        assertEquals(3, stock.get("pen"));                     // input untouched
    }

    @Test
    void accountBothWays() {
        var acc = new Account();
        acc.deposit(100);
        acc.withdraw(30);
        assertEquals(70, acc.balance());
        assertEquals(70, OopVsFp.balanceFromEvents(List.of(new Deposited(100), new Withdrew(30))));
    }

    @Test
    void strategyBothWays() {
        assertEquals(50, OopVsFp.checkoutOop(100, new BlackFriday()));
        assertEquals(50, OopVsFp.checkoutFp(100, b -> b * 0.5));
    }

    @Test
    void concurrencyBothWays() throws Exception {
        var chunks = IntStream.range(0, 100).mapToObj(i -> "a b a").toList();
        var shared = new SharedCounter();
        try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {   // virtual threads (Java 21+)
            chunks.forEach(c -> pool.submit(() -> shared.addText(c)));
        }
        assertEquals(200L, shared.counts().get("a"));
        assertEquals(Map.of("a", 200L, "b", 100L), Map.copyOf(OopVsFp.parallelCount(chunks)));
    }

    @Test
    void hybridImmutableUpdate() {
        var s = new Settings();
        assertEquals("dark", s.withTheme("dark").theme());
        assertEquals("light", s.theme());
    }
}
