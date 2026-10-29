package oop.lifecycle;

import java.lang.ref.Cleaner;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** Object lifecycle in Java: try-with-resources, Cleaner, weak references and GC. */
public final class Lifecycle {
    private Lifecycle() {}

    /** A resource that must be closed: implement AutoCloseable and use try-with-resources. */
    public static final class Connection implements AutoCloseable {
        private static final AtomicInteger OPEN = new AtomicInteger();
        private final String name;
        private boolean closed;

        public Connection(String name) { this.name = name; OPEN.incrementAndGet(); }
        public static int openCount() { return OPEN.get(); }
        public boolean isClosed() { return closed; }
        public String name() { return name; }

        @Override public void close() {               // ALWAYS called at the end of try(...), even on exceptions
            if (!closed) { closed = true; OPEN.decrementAndGet(); }
        }
    }

    /** Transaction: commit on normal exit, rollback when the body throws. */
    public static void transaction(List<String> log, Runnable body) {
        log.add("begin");
        try {
            body.run();
            log.add("commit");
        } catch (RuntimeException e) {
            log.add("rollback");
            throw e;
        }
    }

    /** Cleaner: a safety net (NOT a replacement for close()) instead of the removed-for-removal finalize(). */
    private static final Cleaner CLEANER = Cleaner.create();
    public static final class NativeBuffer implements AutoCloseable {
        private final Cleaner.Cleanable cleanable;
        public NativeBuffer(AtomicInteger released) {
            cleanable = CLEANER.register(this, released::incrementAndGet);   // action must not capture `this`
        }
        @Override public void close() { cleanable.clean(); }               // runs the action at most once
    }

    static final class Node { Node other; }

    /** Java's tracing GC collects cycles: a weak reference does not keep the object alive. */
    public static boolean cycleIsCollected() throws InterruptedException {
        var a = new Node();
        var b = new Node();
        a.other = b;
        b.other = a;
        var probe = new WeakReference<>(a);
        a = null;
        b = null;
        for (int i = 0; i < 50 && probe.get() != null; i++) {   // System.gc() is only a hint
            System.gc();
            Thread.sleep(10);
        }
        return probe.get() == null;
    }
}
