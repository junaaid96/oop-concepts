package oop.solid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** SOLID principles applied to a small invoicing feature. */
public final class Solid {
    private Solid() {}

    public record LineItem(String name, double price, int qty) {}

    // S — each class has ONE reason to change --------------------------------------
    public static final class InvoiceCalculator {                 // changes only if pricing rules change
        private final List<Discount> discounts;
        public InvoiceCalculator(List<Discount> discounts) { this.discounts = List.copyOf(discounts); }

        public double total(List<LineItem> items) {
            double subtotal = items.stream().mapToDouble(i -> i.price() * i.qty()).sum();
            for (Discount d : discounts) subtotal = d.apply(subtotal);   // O — new discounts plug in
            return Math.round(subtotal * 100) / 100.0;
        }
    }

    public static final class InvoiceFormatter {                  // changes only if the format changes
        public String format(List<LineItem> items, double total) {
            return Stream.concat(items.stream().map(i -> i.name() + " x" + i.qty()),
                            Stream.of("TOTAL %.2f".formatted(total)))
                    .collect(Collectors.joining("\n"));
        }
    }

    // O — extension point -------------------------------------------------------------
    @FunctionalInterface
    public interface Discount { double apply(double total); }

    public record FlatDiscount(double amount) implements Discount {
        public double apply(double total) { return Math.max(0, total - amount); }
    }

    // L — substitutable subtypes ------------------------------------------------------
    public interface Bird { String move(); }                    // contract: every bird can MOVE (not fly)
    public static final class Sparrow implements Bird { public String move() { return "flies"; } }
    public static final class Penguin implements Bird { public String move() { return "swims"; } }  // no fly() that throws

    // I — small interfaces --------------------------------------------------------------
    @FunctionalInterface
    public interface Sender { void send(String to, String body); }

    // D — the high-level policy depends on abstractions; details are injected ----------
    public static final class InvoiceService {
        private final InvoiceCalculator calc;
        private final InvoiceFormatter fmt;
        private final Sender sender;

        public InvoiceService(InvoiceCalculator calc, InvoiceFormatter fmt, Sender sender) {
            this.calc = calc;
            this.fmt = fmt;
            this.sender = sender;
        }

        public double bill(String customer, List<LineItem> items) {
            double total = calc.total(items);
            sender.send(customer, fmt.format(items, total));
            return total;
        }
    }

    /** Test double that satisfies the Sender interface. */
    public static final class FakeSender implements Sender {
        public record Sent(String to, String body) {}
        private final List<Sent> sent = new ArrayList<>();
        @Override public void send(String to, String body) { sent.add(new Sent(to, body)); }
        public List<Sent> sent() { return List.copyOf(sent); }
    }
}
