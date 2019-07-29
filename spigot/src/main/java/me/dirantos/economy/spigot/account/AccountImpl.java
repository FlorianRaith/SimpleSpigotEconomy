package me.dirantos.economy.spigot.account;

import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.transaction.Transaction;

import java.util.*;

public final class AccountImpl implements Account {

    private int accountNumber;
    private final UUID owner;
    private double balance;
    private final Set<Integer> transactions;

    public AccountImpl(int accountNumber, UUID owner, double balance, Set<Integer> transactions) {
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

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public int getTransactionsAmount() {
        return transactions.size();
    }

    @Override
    public Set<Integer> getTransactionIDs() {
        return Collections.unmodifiableSet(transactions);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction.getID());
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
