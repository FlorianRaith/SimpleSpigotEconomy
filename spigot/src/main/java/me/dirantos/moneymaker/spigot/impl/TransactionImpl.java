package me.dirantos.moneymaker.spigot.impl;

import me.dirantos.moneymaker.api.Account;
import me.dirantos.moneymaker.api.Transaction;
import me.dirantos.moneymaker.api.TransactionType;

import java.util.Date;

public class TransactionImpl implements Transaction {

    private final int id;
    private final Account recipient;
    private final double amount;
    private final Date date;
    private final TransactionType type;

    public TransactionImpl(Account recipient, double amount, Date date, TransactionType type) {
        this.id = 0;
        this.recipient = recipient;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public Account getRecipient() {
        return recipient;
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

}
