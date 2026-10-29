package oop.di;

import java.time.Clock;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/** Dependency injection by hand: constructor injection, test doubles, a tiny container, ScopedValue. */
public final class Di {
    private Di() {}

    public record User(int id, String email) {}

    public interface UserRepository {
        void save(User u);
        Optional<User> find(int id);
    }

    public interface Notifier { void notify(String to, String msg); }

    /** Depends on abstractions, receives them through the constructor (no `new` inside). */
    public static final class SignupService {
        private final UserRepository repo;
        private final Notifier notifier;
        private final Clock clock;           // even time is a dependency -> deterministic tests

        public SignupService(UserRepository repo, Notifier notifier, Clock clock) {
            this.repo = repo;
            this.notifier = notifier;
            this.clock = clock;
        }

        public User signup(int id, String email) {
            var u = new User(id, email);
            repo.save(u);
            notifier.notify(email, greeting() + ", welcome!");
            return u;
        }

        String greeting() {
            return LocalTime.now(clock).getHour() < 12 ? "Good morning" : "Good evening";
        }
    }

    // ---- test doubles ---------------------------------------------------------------
    public static final class InMemoryRepo implements UserRepository {        // fake
        private final Map<Integer, User> data = new HashMap<>();
        public void save(User u) { data.put(u.id(), u); }
        public Optional<User> find(int id) { return Optional.ofNullable(data.get(id)); }
    }

    public static final class RecordingNotifier implements Notifier {         // spy
        public final List<String> messages = new ArrayList<>();
        public void notify(String to, String msg) { messages.add(to + ": " + msg); }
    }

    /** A tiny DI container: maps a type to a factory. Real ones: Spring, Guice, Dagger. */
    public static final class Container {
        private final Map<Class<?>, Supplier<?>> factories = new HashMap<>();
        private final Map<Class<?>, Object> singletons = new HashMap<>();

        public <T> void singleton(Class<T> type, Supplier<? extends T> factory) {
            factories.put(type, () -> singletons.computeIfAbsent(type, k -> factory.get()));
        }

        public <T> void transientBean(Class<T> type, Supplier<? extends T> factory) {
            factories.put(type, factory);
        }

        public <T> T get(Class<T> type) {
            var f = factories.get(type);
            if (f == null) throw new IllegalStateException("no binding for " + type.getSimpleName());
            return type.cast(f.get());
        }
    }

    /** ScopedValue (final in Java 25): pass context implicitly down a call chain, immutably. */
    public static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();

    public static String handle() {
        return "handled " + (REQUEST_ID.isBound() ? REQUEST_ID.get() : "no-request");
    }

    public static String inRequest(String id) {
        return ScopedValue.where(REQUEST_ID, id).call(Di::handle);
    }
}
