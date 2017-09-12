package me.dirantos.moneymaker.api;

import java.util.List;
import java.util.UUID;

public interface Account {

    int getAccountNumber();

    double getBalance();

    UUID getOwner();

    List<Transaction> getTransactions();

    int getTransactionsAmount();

    List<Transfer> getTransfers();

    List<Transfer> getTransfers(TransferMethod method);

    List<Transaction> getDeposits();

    List<Transaction> getWithdrawals();

    List<Interest> getInterests();

    Transfer transfer(Account account, double amount);

    Interest receiveInterest(double interestRate);

    Transaction deposit(int amount);

    Transaction withdrawal(int amount);

}
