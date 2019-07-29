package me.dirantos.economy.spigot.models;

import me.dirantos.economy.api.models.Transaction;
import me.dirantos.economy.api.models.TransactionType;

import java.util.Date;

public class TransactionImpl implements Transaction {

    private int id;
    private final int recipientAccountNumber;
    private final double amount;
    private final Date date;
    private final TransactionType type;

    public TransactionImpl(int id, int recipientAccountNumber, double amount, Date date, TransactionType type) {
        this.id = id;
        this.recipientAccountNumber = recipientAccountNumber;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public int getRecipientAccountNumber() {
        return recipientAccountNumber;
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
                ", recipientAccountNumber=" + recipientAccountNumber +
                ", amount=" + amount +
                ", date=" + date +
                ", type=" + type +
                '}';
    }
}
