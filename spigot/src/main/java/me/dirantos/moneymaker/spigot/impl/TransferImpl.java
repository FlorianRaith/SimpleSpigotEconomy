package me.dirantos.moneymaker.spigot.impl;

import me.dirantos.moneymaker.api.Account;
import me.dirantos.moneymaker.api.TransactionType;
import me.dirantos.moneymaker.api.Transfer;

import java.util.Date;

public final class TransferImpl extends TransactionImpl implements Transfer {

    private final Account sender;

    public TransferImpl(Account recipient, double amount, Date date, Account sender) {
        super(recipient, amount, date, TransactionType.TRANSFER);
        this.sender = sender;
    }

    @Override
    public Account getSender() {
        return sender;
    }

}
