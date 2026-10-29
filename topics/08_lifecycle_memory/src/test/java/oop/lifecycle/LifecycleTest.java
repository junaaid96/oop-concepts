package oop.lifecycle;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import oop.lifecycle.Lifecycle.*;
import org.junit.jupiter.api.Test;

class LifecycleTest {
    @Test
    void tryWithResourcesClosesOnError() {
        int start = Connection.openCount();
        var holder = new Connection[1];
        assertThrows(IllegalStateException.class, () -> {
            try (var conn = new Connection("db")) {
                holder[0] = conn;
                throw new IllegalStateException("boom");
            }
        });
        assertTrue(holder[0].isClosed());
        assertEquals(start, Connection.openCount());
    }

    @Test
    void transaction() {
        List<String> log = new ArrayList<>();
        Lifecycle.transaction(log, () -> {});
        assertThrows(IllegalArgumentException.class,
                () -> Lifecycle.transaction(log, () -> { throw new IllegalArgumentException(); }));
        assertEquals(List.of("begin", "commit", "begin", "rollback"), log);
    }

    @Test
    void cleanerRunsOnce() {
        var released = new AtomicInteger();
        try (var buf = new NativeBuffer(released)) {
            assertNotNull(buf);
        }
        assertEquals(1, released.get());
    }

    @Test
    void cyclesAreCollected() throws InterruptedException {
        assertTrue(Lifecycle.cycleIsCollected());
    }
}
