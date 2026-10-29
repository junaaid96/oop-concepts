"""A domain exception hierarchy with chaining and a strong-guarantee transfer."""
from __future__ import annotations


class AppError(Exception):
    """Root of all errors raised by this application."""


class PaymentError(AppError):
    """Anything that went wrong while paying."""


class InsufficientFunds(PaymentError):
    def __init__(self, needed: float, available: float) -> None:
        super().__init__(f"needed {needed}, available {available}")
        self.needed = needed                # structured data for handlers
        self.available = available


class GatewayError(PaymentError):
    pass


class Wallet:
    def __init__(self, owner: str, balance: float = 0) -> None:
        if balance < 0:
            raise ValueError("balance must be >= 0")   # fail fast in constructor
        self.owner = owner
        self.balance = balance

    def pay(self, amount: float) -> None:
        if amount > self.balance:
            raise InsufficientFunds(amount, self.balance)
        self.balance -= amount


def transfer(src: Wallet, dst: Wallet, amount: float) -> None:
    """Strong guarantee: either both wallets change or neither does."""
    if amount > src.balance:               # validate BEFORE mutating anything
        raise InsufficientFunds(amount, src.balance)
    src.balance -= amount
    dst.balance += amount


def charge_card(gateway) -> str:
    try:
        return gateway()
    except ConnectionError as e:
        raise GatewayError("card gateway unavailable") from e   # keep the cause
