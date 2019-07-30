package me.dirantos.economy.spigot.transaction;

import me.dirantos.economy.api.transaction.TransactionType;
import me.dirantos.economy.api.transaction.Transfer;

import java.util.Date;

public final class TransferImpl extends TransactionImpl implements Transfer {

    private final int senderAccountID;
    private final int recipientAccountID;

    public TransferImpl(int id, int accountID, double amount, Date date, int senderAccountID, int recipientAccountID) {
        super(id, accountID, amount, date, TransactionType.TRANSFER);
        this.senderAccountID = senderAccountID;
        this.recipientAccountID = recipientAccountID;
    }

    @Override
    public int getSenderAccountID() {
        return senderAccountID;
    }

    @Override
    public int getRecipientAccountID() {
        return recipientAccountID;
    }
}
