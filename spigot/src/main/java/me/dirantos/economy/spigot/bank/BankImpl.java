package me.dirantos.economy.spigot.bank;

import me.dirantos.economy.api.bank.Bank;

import java.util.UUID;

public final class BankImpl implements Bank {

    private final UUID owner;
    private double walletBalance;
    private double bankBalance;

    public BankImpl(UUID owner, double walletBalance, double bankBalance) {
        this.owner = owner;
        this.walletBalance = walletBalance;
        this.bankBalance = bankBalance;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    @Override
    public double getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(double bankBalance) {
        this.bankBalance = bankBalance;
    }

    @Override
    public String toString() {
        return "BankImpl{" +
                "owner=" + owner +
                ", walletBalance=" + walletBalance +
                ", bankBalance=" + bankBalance +
                '}';
    }
}
