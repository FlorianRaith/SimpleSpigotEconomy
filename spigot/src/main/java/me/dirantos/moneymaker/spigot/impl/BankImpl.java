package me.dirantos.moneymaker.spigot.impl;

import me.dirantos.moneymaker.api.Account;
import me.dirantos.moneymaker.api.Bank;
import me.dirantos.moneymaker.api.Interest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class BankImpl implements Bank {

    private final UUID owner;
    private final List<Account> accounts = new ArrayList<>();
    private double money;

    public BankImpl(UUID owner, double money) {
        this.owner = owner;
        this.money = money;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public CompletableFuture<Void> loadAccounts() {
        return null;
    }

    @Override
    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    @Override
    public double getTotalBalance() {
        return accounts.stream().map(Account::getBalance).reduce(0.0, Double::sum);
    }

    @Override
    public List<Interest> receiveInterest(double interestRate) {
        List<Interest> interests = new ArrayList<>();
        for(Account account : accounts) {
            interests.add(account.receiveInterest(interestRate));
        }
        return interests;
    }

    @Override
    public double getMoney() {
        return money;
    }

    @Override
    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public void addMoney(double amount) {
        money += amount;
    }

    @Override
    public void removeMoney(double amount) {
        money -= amount;
    }

    @Override
    public String toString() {
        return "BankImpl{" +
                "owner=" + owner +
                ", money=" + money +
                '}';
    }
}
