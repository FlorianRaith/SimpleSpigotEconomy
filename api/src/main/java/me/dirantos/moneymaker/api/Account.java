package me.dirantos.moneymaker.api;

import java.util.List;
import java.util.UUID;

public interface Account {

    int getAccountNumber();

    double getBalance();

    UUID getOwner();

    List<Transaction> getTransactions();

    int getTransationsAmount();

    List<Transfer> getTransfers();

    List<Transfer> getTransfers(TransferMethod method);

    List<Transaction> getDeposits();

    List<Transaction> getWithdrawals();

    List<Interest> getInterests();

    Transfer transfer(Account account, double amount);

    Transfer transfer(int accountNumber, double amount);

    Transaction deposit(int amount);

    Transaction withdrawal(int amount);

}
