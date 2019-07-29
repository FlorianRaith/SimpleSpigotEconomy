package me.dirantos.economy.api.transaction;

import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.transaction.Interest;
import me.dirantos.economy.api.transaction.Transaction;
import me.dirantos.economy.api.transaction.Transfer;

import java.util.Optional;
import java.util.Set;

public interface TransactionManager {

    Transfer makeTransfer(Account recipient, Account sender, double amount);

    Interest makeInterest(Account recipient, double interestRate);

    Transaction makeWithdrawal(Account recipient, double amount);

    Transaction makeDeposit(Account recipient, double amount);

    Optional<Transaction> loadTransaction(int id);

    Set<Transaction> loadTransactions(Set<Integer> ids);

    void deleteTransaction(Transaction transaction);

    void deleteTransaction(int id);

}
