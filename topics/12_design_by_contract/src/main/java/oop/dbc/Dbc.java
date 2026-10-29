package oop.dbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Design by contract: preconditions (caller's duty), postconditions (method's promise)
 * and class invariants (always true between public calls).
 */
public final class Dbc {
    private Dbc() {}

    public static final class ContractViolation extends RuntimeException {
        public ContractViolation(String msg) { super(msg); }
    }

    static void require(boolean cond, String msg) {
        if (!cond) throw new ContractViolation("precondition failed: " + msg);
    }

    static void ensure(boolean cond, String msg) {
        if (!cond) throw new ContractViolation("postcondition failed: " + msg);
    }

    public static final class Account {
        private double balance;

        public double balance() { return balance; }

        public void deposit(double amount) {
            require(amount > 0, "amount > 0");
            double old = balance;
            balance += amount;
            ensure(balance == old + amount, "balance increased by amount");
            checkInvariant();
        }

        public void withdraw(double amount) {
            require(amount > 0 && amount <= balance, "0 < amount <= balance");
            balance -= amount;
            checkInvariant();
        }

        private void checkInvariant() {
            // `assert` is only checked with -ea (Surefire enables it in tests); never use it for input validation
            assert balance >= 0 : "invariant: balance >= 0";
        }
    }

    /** Invariant: items are always sorted. */
    public static final class SortedList {
        private final List<Integer> items = new ArrayList<>();

        public void insert(int x) {
            int i = 0;
            while (i < items.size() && items.get(i) < x) i++;
            items.add(i, x);
            ensure(isSorted(), "list stays sorted");
        }

        public List<Integer> items() { return List.copyOf(items); }

        private boolean isSorted() {
            for (int i = 1; i < items.size(); i++) if (items.get(i - 1) > items.get(i)) return false;
            return true;
        }
    }

    /** Standard JDK precondition helpers. */
    public static String greet(String name) {
        Objects.requireNonNull(name, "name");
        Objects.checkIndex(0, name.length());     // throws IndexOutOfBounds for ""
        return "hello " + name;
    }
}
