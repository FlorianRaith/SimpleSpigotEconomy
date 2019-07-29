package me.dirantos.economy.spigot.models;

import me.dirantos.economy.api.models.Bank;

import java.util.List;
import java.util.UUID;

public final class BankImpl implements Bank {

    private final UUID owner;
    private final List<Integer> accountNumbers;
    private double money;

    public BankImpl(UUID owner, List<Integer> accountNumbers, double money) {
        this.owner = owner;
        this.accountNumbers = accountNumbers;
        this.money = money;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public List<Integer> getAccountNumbers() {
        return accountNumbers;
    }

    public void addAccount(int accountNumber) {
        accountNumbers.add(accountNumber);
    }

    public void removeAccount(int accountNumber) {
        accountNumbers.remove(accountNumbers.indexOf(accountNumber));
    }

    @Override
    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "BankImpl{" +
                "owner=" + owner +
                ", accountNumbers=" + accountNumbers +
                ", money=" + money +
                '}';
    }

}
