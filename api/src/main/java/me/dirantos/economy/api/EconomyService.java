package me.dirantos.economy.api;

import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.bank.Bank;
import me.dirantos.economy.api.transaction.Interest;
import me.dirantos.economy.api.transaction.Transaction;
import me.dirantos.economy.api.transaction.Transfer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface EconomyService {

    EconomyRepository getRepository();

    Bank loadBank(UUID player);

    Bank loadBank(Player player);

    void setWalletBalance(Player player, double amount);

    void setWalletBalance(UUID player, double amount);

    void setWalletBalance(Bank bank, double amount);

    void addWalletBalance(Player player, double amount);

    void addWalletBalance(UUID player, double amount);

    void addWalletBalance(Bank bank, double amount);

    void removeWalletBalance(Player player, double amount);

    void removeWalletBalance(UUID player, double amount);

    void removeWalletBalance(Bank bank, double amount);

    void deleteBank(UUID player);

    void deleteBank(Player player);

    void deleteBank(Bank bank);

    Account createNewAccount(UUID player, double startBalance);

    Account createNewAccount(Player player, double startBalance);

    Account createNewAccount(UUID player);

    Account createNewAccount(Player player);

    Optional<Account> loadAccount(int accountID);

    Set<Account> loadBankAccounts(Bank bank);

    Set<Account> loadPlayerAccounts(UUID player);

    Set<Account> loadPlayerAccounts(Player player);

    void deleteAccount(Account account);

    void deleteAccount(int accountID);

    Transfer transfer(int senderAccountID, int recipientAccountID, double amount);

    Transfer transfer(Account sender, Account recipient, double amount);

    Interest interest(int accountID, double interestRate);

    Interest interest(Account recipient, double interestRate);

    Transaction withdrawal(int accountID, double amount);

    Transaction withdrawal(Account recipient, double amount);

    Transaction withdrawal(Bank bank, int accountID, double amount);

    Transaction withdrawal(Bank bank, Account recipient, double amount);

    Transaction deposit(int accountID, double amount);

    Transaction deposit(Account recipient, double amount);

    Transaction deposit(Bank bank, int accountID, double amount);

    Transaction deposit(Bank bank, Account recipient, double amount);

    Optional<Transaction> loadTransaction(int transactionID);

    Set<Transaction> loadAccountTransactions(int accountID);

    Set<Transaction> loadAccountTransactions(Account account);

    void deleteTransaction(int transactionID);

    void deleteTransaction(Transaction transaction);

}