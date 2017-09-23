package me.dirantos.moneymaker.spigot.models;

import me.dirantos.moneymaker.api.models.Account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class AccountImpl implements Account {

    private int accountNumber;
    private final UUID owner;
    private double balance;
    // list for saving the balance when it will change to get the average balance for calculating the interest
    private List<Double> balanceChanges = new ArrayList<>();
    private final List<Integer> transactions;

    public AccountImpl(int accountNumber, UUID owner, double balance, List<Integer> transactions) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = balance;
        this.transactions = transactions;
    }

    @Override
    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public int getTransactionsAmount() {
        return transactions.size();
    }

    @Override
    public List<Integer> getTransactionIDs() {
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public String toString() {
        return "AccountImpl{" +
                "accountNumber=" + accountNumber +
                ", owner=" + owner +
                ", balance=" + balance +
                ", transactions=" + transactions +
                '}';
    }
}
