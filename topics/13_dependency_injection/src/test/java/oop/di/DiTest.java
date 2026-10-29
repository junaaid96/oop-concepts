package oop.di;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import oop.di.Di.*;
import org.junit.jupiter.api.Test;

class DiTest {
    private static Clock at(String isoUtc) { return Clock.fixed(Instant.parse(isoUtc), ZoneOffset.UTC); }

    @Test
    void constructorInjectionWithFakes() {
        var repo = new InMemoryRepo();
        var notifier = new RecordingNotifier();
        var service = new SignupService(repo, notifier, at("2026-01-01T08:00:00Z"));
        service.signup(1, "ana@example.com");
        assertTrue(repo.find(1).isPresent());
        assertEquals("ana@example.com: Good morning, welcome!", notifier.messages.getFirst());
        assertEquals("Good evening", new SignupService(repo, notifier, at("2026-01-01T20:00:00Z")).greeting());
    }

    @Test
    void container() {
        var c = new Container();
        c.singleton(UserRepository.class, InMemoryRepo::new);
        c.transientBean(Notifier.class, RecordingNotifier::new);
        assertSame(c.get(UserRepository.class), c.get(UserRepository.class));
        assertNotSame(c.get(Notifier.class), c.get(Notifier.class));
        assertThrows(IllegalStateException.class, () -> c.get(String.class));
    }

    @Test
    void scopedValue() {
        assertEquals("handled r-42", Di.inRequest("r-42"));
        assertEquals("handled no-request", Di.handle());
    }
}
