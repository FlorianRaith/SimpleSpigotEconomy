package me.dirantos.economy.api;

import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.bank.Bank;
import me.dirantos.economy.api.transaction.Interest;
import me.dirantos.economy.api.transaction.Transaction;
import me.dirantos.economy.api.transaction.Transfer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface EconomyRepository {

    void initialize();

    @NotNull
    Bank createBank(@NotNull UUID owner);

    @NotNull
    Optional<Bank> findBank(@NotNull UUID owner);

    void updateBank(@NotNull Bank bank);

    void deleteBank(@NotNull UUID owner);

    @NotNull
    Account createAccount(@NotNull UUID owner, double startBalance);

    @NotNull
    Optional<Account> findAccount(int accountID);

    @NotNull
    Set<Account> findAllBankAccounts(@NotNull UUID owner);

    void updateAccount(@NotNull Account account);

    void deleteAccount(int accountID);

    void saveTransaction(@NotNull Bank bank, @NotNull Transaction transaction, @NotNull Account account);

    void saveInterest(@NotNull Interest transaction, @NotNull Account account);

    void saveTransfer(@NotNull Transfer senderTransfer, @NotNull Transfer receiverTransfer, @NotNull Account sender, @NotNull Account receiver);

    @NotNull
    Set<Transaction> findAllAccountTransactions(int accountID);

    void deleteTransaction(int transactionID);
}
