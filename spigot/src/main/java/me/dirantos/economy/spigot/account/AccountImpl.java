package me.dirantos.economy.spigot.account;

import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.transaction.Transaction;

import java.util.*;

public final class AccountImpl implements Account {

    private int id;
    private final UUID owner;
    private double balance;

    public AccountImpl(int id, UUID owner, double balance) {
        this.id = id;
        this.owner = owner;
        this.balance = balance;
    }

    public int getID() {
        return id;
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
    public String toString() {
        return "AccountImpl{" +
                "id=" + id +
                ", owner=" + owner +
                ", balance=" + balance +
                '}';
    }
}
