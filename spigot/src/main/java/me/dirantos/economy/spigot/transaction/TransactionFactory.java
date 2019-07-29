package me.dirantos.economy.spigot.transaction;

import me.dirantos.economy.api.transaction.Interest;
import me.dirantos.economy.api.transaction.Transaction;
import me.dirantos.economy.api.transaction.TransactionType;
import me.dirantos.economy.api.transaction.Transfer;
import me.dirantos.economy.spigot.transaction.InterestImpl;
import me.dirantos.economy.spigot.transaction.TransactionImpl;
import me.dirantos.economy.spigot.transaction.TransferImpl;

import java.util.Date;

public final class TransactionFactory {

    public Transaction createTransaction(TransactionType type, int recipient, int sender, double amount, double interestRate) {
        switch(type) {
            case TRANSFER:
                return new TransferImpl(-1, recipient, amount, new Date(), sender);
            case INTEREST:
                return new InterestImpl(-1, recipient, amount, new Date(), interestRate);
            default:
                return new TransactionImpl(-1, recipient, amount, new Date(), type);
        }
    }

    public Transfer createTransfer(int recipient, int sender, double amount) {
        return (Transfer) createTransaction(TransactionType.TRANSFER, recipient, sender, amount, 0);
    }

    public Interest createInterest(int recipient, double amount, double interestRate) {
        return (Interest) createTransaction(TransactionType.INTEREST, recipient, 0, amount, interestRate);
    }

    public Transaction createWithdrawal(int account, double amount) {
        return createTransaction(TransactionType.WITHDRAWAL, account, 0, amount, 0);
    }

    public Transaction createDeposit(int account, double amount) {
        return createTransaction(TransactionType.DEPOSIT, account, 0, amount, 0);
    }

}
