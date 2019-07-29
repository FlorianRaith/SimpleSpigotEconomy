package me.dirantos.economy.spigot.models;

import me.dirantos.economy.api.models.TransactionType;
import me.dirantos.economy.api.models.Transfer;

import java.util.Date;

public final class TransferImpl extends TransactionImpl implements Transfer {

    private final int senderAccountNumber;

    public TransferImpl(int id, int recipient, double amount, Date date, int senderAccountNumber) {
        super(id, recipient, amount, date, TransactionType.TRANSFER);
        this.senderAccountNumber = senderAccountNumber;
    }

    @Override
    public int getSenderAccountNumber() {
        return senderAccountNumber;
    }

    @Override
    public String toString() {
        return "TransferImpl{" +
                "senderAccountNumber=" + senderAccountNumber +
                "} " + super.toString();
    }

}
