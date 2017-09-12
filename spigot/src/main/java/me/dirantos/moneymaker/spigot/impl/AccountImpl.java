package me.dirantos.moneymaker.spigot.impl;

import me.dirantos.moneymaker.api.*;
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

public final class AccountImpl implements Account {

    private final int accountNumber;
    private final UUID owner;
    private double balance;
    // list for saving the balance when it will change to get the average balance for calculating the interest
    private List<Double> balanceChanges = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();

    public AccountImpl(int accountNumber, UUID owner) {
        this.accountNumber = accountNumber;
        this.owner = owner;
    }

    @Override
    public int getAccountNumber() {
        return accountNumber;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public int getTransactionsAmount() {
        return transactions.size();
    }

    @Override
    public List<Transfer> getTransfers() {
        return Collections.unmodifiableList(transactions.stream().filter(trans -> trans.getType() == TransactionType.TRANSFER).map(trans -> (Transfer) trans).collect(Collectors.toList()));
    }

    @Override
    public List<Transfer> getTransfers(TransferMethod method) {
        return Collections.unmodifiableList(transactions.stream().filter(trans -> trans.getType() == TransactionType.TRANSFER).map(trans -> (Transfer) trans)
                .filter(trans -> method == TransferMethod.INCOMING ? trans.getRecipient() == this : trans.getSender() == this).collect(Collectors.toList()));
    }

    @Override
    public List<Transaction> getDeposits() {
        return Collections.unmodifiableList(transactions.stream().filter(trans -> trans.getType() == TransactionType.DEPOSIT).collect(Collectors.toList()));
    }

    @Override
    public List<Transaction> getWithdrawals() {
        return Collections.unmodifiableList(transactions.stream().filter(trans -> trans.getType() == TransactionType.WITHDRAWAL).collect(Collectors.toList()));
    }

    @Override
    public List<Interest> getInterests() {
        return Collections.unmodifiableList(transactions.stream().filter(trans -> trans.getType() == TransactionType.INTEREST).map(trans -> (Interest) trans).collect(Collectors.toList()));
    }

    @Override
    public Transfer transfer(Account account, double amount) {
        if(account instanceof GodAccount) throw new InvalidParameterException("account can't be a GodAccount!");
        if(getBalance() - amount < 0) throw new IllegalStateException("The account has not enough money!");
        ((AccountImpl) account).addBalance(amount);
        removeBalance(amount);
        Transfer transfer = new TransferImpl(account, amount, new Date(), this);
        transactions.add(transfer);
        ((AccountImpl) account).addTransaction(transfer);
        return transfer;
    }

    @Override
    public Interest receiveInterest(double interestRate) {
        double averageBalance = balanceChanges.stream().reduce(0.0, Double::sum) / 2.0;
        balanceChanges.clear();
        addBalance(averageBalance * interestRate);
        Interest interest = new InterestImpl(this, averageBalance * interestRate, new Date(), interestRate);
        transactions.add(interest);
        return interest;
    }

    @Override
    public Transaction deposit(int amount) {
        return null;
    }

    @Override
    public Transaction withdrawal(int amount) {
        return null;
    }

    void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    void setBalance(double balance) {
        this.balance = balance;
        balanceChanges.add(balance);
    }

    void addBalance(double amount) {
        setBalance(getBalance() + amount);
    }

    void removeBalance(double amount) {
        setBalance(getBalance() - amount);
    }



}
