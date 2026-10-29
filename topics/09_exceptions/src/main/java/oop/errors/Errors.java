package oop.errors;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.Callable;

/** A domain exception hierarchy, chaining, checked vs unchecked, and a strong-guarantee transfer. */
public final class Errors {
    private Errors() {}

    /** Root of all errors raised by this application (unchecked). */
    public static class AppException extends RuntimeException {
        public AppException(String msg) { super(msg); }
        public AppException(String msg, Throwable cause) { super(msg, cause); }
    }

    public static class PaymentException extends AppException {
        public PaymentException(String msg) { super(msg); }
        public PaymentException(String msg, Throwable cause) { super(msg, cause); }
    }

    /** Carries structured data so handlers don't have to parse the message. */
    public static final class InsufficientFundsException extends PaymentException {
        private final double needed, available;
        public InsufficientFundsException(double needed, double available) {
            super("needed " + needed + ", available " + available);
            this.needed = needed;
            this.available = available;
        }
        public double needed() { return needed; }
        public double available() { return available; }
    }

    public static final class GatewayException extends PaymentException {
        public GatewayException(String msg, Throwable cause) { super(msg, cause); }
    }

    public static final class Wallet {
        private final String owner;
        private double balance;

        public Wallet(String owner, double balance) {
            if (balance < 0) throw new IllegalArgumentException("balance must be >= 0");   // fail fast
            this.owner = owner;
            this.balance = balance;
        }
        public String owner() { return owner; }
        public double balance() { return balance; }

        public void pay(double amount) {
            if (amount > balance) throw new InsufficientFundsException(amount, balance);
            balance -= amount;
        }
    }

    /** Strong guarantee: either both wallets change or neither does. */
    public static void transfer(Wallet src, Wallet dst, double amount) {
        if (amount > src.balance) throw new InsufficientFundsException(amount, src.balance);   // validate first
        src.balance -= amount;
        dst.balance += amount;
    }

    /** Translate a low-level CHECKED exception into a domain exception, keeping the cause. */
    public static String chargeCard(Callable<String> gateway) {
        try {
            return gateway.call();
        } catch (IOException e) {
            throw new GatewayException("card gateway unavailable", e);
        } catch (Exception e) {
            throw new AppException("unexpected", e);
        }
    }

    /** Checked exceptions must be declared or handled; wrap them when a lambda can't throw. */
    public static String readConfig(boolean fail) throws IOException {
        if (fail) throw new IOException("missing file");
        return "ok";
    }

    public static String readConfigUnchecked(boolean fail) {
        try {
            return readConfig(fail);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Multi-catch and try-with-resources suppressed exceptions. */
    public static Throwable[] suppressedDemo() {
        try (AutoCloseable r = () -> { throw new IllegalStateException("close failed"); }) {
            throw new IllegalArgumentException("body failed");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return e.getSuppressed();
        } catch (Exception e) {
            throw new AppException("unexpected", e);
        }
    }
}
