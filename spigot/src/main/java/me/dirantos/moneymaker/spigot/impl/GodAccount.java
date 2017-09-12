package me.dirantos.moneymaker.spigot.impl;

import me.dirantos.moneymaker.api.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public final class GodAccount implements Account {

    private final UUID uuid = UUID.randomUUID();

    @Override
    public int getAccountNumber() {
        return 000000;
    }

    @Override
    public double getBalance() {
        return 0;
    }

    @Override
    public UUID getOwner() {
        return uuid;
    }

    @Override
    public List<Transaction> getTransactions() {
        return new ArrayList<>();
    }

    @Override
    public int getTransactionsAmount() {
        return 0;
    }

    @Override
    public List<Transfer> getTransfers() {
        return new ArrayList<>();
    }

    @Override
    public List<Transfer> getTransfers(TransferMethod method) {
        return new ArrayList<>();
    }

    @Override
    public List<Transaction> getDeposits() {
        return new ArrayList<>();
    }

    @Override
    public List<Transaction> getWithdrawals() {
        return new ArrayList<>();
    }

    @Override
    public List<Interest> getInterests() {
        return new ArrayList<>();
    }

    @Override
    public Transfer transfer(Account account, double amount) {
        if(account instanceof GodAccount) throw new InvalidParameterException("account can't be a GodAccount!");
        ((AccountImpl) account).addBalance(amount);
        Transfer transfer = new TransferImpl(account, amount, new Date(), this);
        ((AccountImpl) account).addTransaction(transfer);
        return transfer;
    }

    @Override
    public Interest receiveInterest(double interestRate) {
        return new InterestImpl(this, 0, new Date(), interestRate);
    }

    @Override
    public Transaction deposit(int amount) {
        return new TransactionImpl(this, 0, new Date(), TransactionType.DEPOSIT);
    }

    @Override
    public Transaction withdrawal(int amount) {
        return new TransactionImpl(this, 0, new Date(), TransactionType.WITHDRAWAL);
    }

}
