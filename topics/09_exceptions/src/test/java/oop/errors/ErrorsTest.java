package oop.errors;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import oop.errors.Errors.*;
import org.junit.jupiter.api.Test;

class ErrorsTest {
    @Test
    void hierarchyAndData() {
        var w = new Wallet("Ana", 10);
        var e = assertThrows(InsufficientFundsException.class, () -> w.pay(25));
        assertInstanceOf(PaymentException.class, e);
        assertInstanceOf(AppException.class, e);
        assertEquals(25, e.needed());
        assertEquals(10, e.available());
        assertThrows(IllegalArgumentException.class, () -> new Wallet("x", -1));
    }

    @Test
    void strongGuarantee() {
        var a = new Wallet("a", 5);
        var b = new Wallet("b", 0);
        assertThrows(InsufficientFundsException.class, () -> Errors.transfer(a, b, 10));
        assertEquals(5, a.balance());
        assertEquals(0, b.balance());
        Errors.transfer(a, b, 5);
        assertEquals(0, a.balance());
        assertEquals(5, b.balance());
    }

    @Test
    void chaining() {
        var e = assertThrows(GatewayException.class,
                () -> Errors.chargeCard(() -> { throw new IOException("timeout"); }));
        assertInstanceOf(IOException.class, e.getCause());
        assertEquals("paid", Errors.chargeCard(() -> "paid"));
    }

    @Test
    void checkedVsUnchecked() throws IOException {
        assertEquals("ok", Errors.readConfig(false));
        assertThrows(IOException.class, () -> Errors.readConfig(true));
        assertThrows(UncheckedIOException.class, () -> Errors.readConfigUnchecked(true));
    }

    @Test
    void suppressed() {
        Throwable[] s = Errors.suppressedDemo();
        assertEquals(1, s.length);
        assertEquals("close failed", s[0].getMessage());
    }
}
