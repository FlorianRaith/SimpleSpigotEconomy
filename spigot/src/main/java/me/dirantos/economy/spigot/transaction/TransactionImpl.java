package me.dirantos.economy.spigot.transaction;

import me.dirantos.economy.api.transaction.Transaction;
import me.dirantos.economy.api.transaction.TransactionType;

import java.util.Date;

public class TransactionImpl implements Transaction {

    private int id;
    private final int accountID;
    private final double amount;
    private final Date date;
    private final TransactionType type;

    public TransactionImpl(int id, int accountID, double amount, Date date, TransactionType type) {
        this.id = id;
        this.accountID = accountID;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public void setID(int id) {
        this.id = id;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public int getAccountID() {
        return accountID;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public TransactionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TransactionImpl{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                ", type=" + type +
                '}';
    }
}
