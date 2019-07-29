package me.dirantos.economy.spigot.managers;

import me.dirantos.economy.api.cache.ModelCache;
import me.dirantos.economy.api.events.AsyncTransactionEvent;
import me.dirantos.economy.api.fetchers.AccountFetcher;
import me.dirantos.economy.api.fetchers.BankFetcher;
import me.dirantos.economy.api.fetchers.TransactionFetcher;
import me.dirantos.economy.api.managers.TransactionManager;
import me.dirantos.economy.api.models.*;
import me.dirantos.economy.api.events.AsyncBankUpdateEvent;
import me.dirantos.economy.spigot.models.AccountImpl;
import me.dirantos.economy.spigot.models.BankImpl;
import me.dirantos.economy.spigot.utils.Utils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

import java.util.*;

public final class TransactionManagerImpl implements TransactionManager {

    private final TransactionFactory factory = new TransactionFactory();
    private final TransactionFetcher transactionFetcher;
    private final AccountFetcher accountFetcher;
    private final BankFetcher bankFetcher;
    private final ModelCache cache;

    public TransactionManagerImpl(TransactionFetcher transactionFetcher, AccountFetcher accountFetcher, BankFetcher bankFetcher, ModelCache cache) {
        this.transactionFetcher = transactionFetcher;
        this.accountFetcher = accountFetcher;
        this.bankFetcher = bankFetcher;
        this.cache = cache;
    }

    @Override
    public Transfer makeTransfer(Account recipient, Account sender, double amount) {
        Transfer transfer = factory.createTransfer(recipient.getAccountNumber(), sender.getAccountNumber(), amount);
        Validate.isTrue(recipient.getAccountNumber() == transfer.getRecipientAccountNumber(), "The given recipient managers does not correspond with the transaction recipient managers-number");
        Validate.isTrue(sender.getAccountNumber() == transfer.getSenderAccountNumber(), "The given sender managers does not correspond with the transaction sender managers-number");

        if(sender.getBalance() - transfer.getAmount() < 0) throw new IllegalStateException("The sender has not enough money");
        ((AccountImpl) sender).setBalance(sender.getBalance() - transfer.getAmount());
        ((AccountImpl) recipient).setBalance(recipient.getBalance() + transfer.getAmount());

        Transaction validated = transactionFetcher.saveData(transfer);

        ((AccountImpl) recipient).addTransaction(validated);
        ((AccountImpl) sender).addTransaction(validated);
        accountFetcher.saveData(recipient);
        accountFetcher.saveData(sender);

        Bank senderBank = Utils.loadBank(sender.getOwner(), cache, bankFetcher);
        Bank recipientBank = Utils.loadBank(recipient.getOwner(), cache, bankFetcher);

        Bukkit.getPluginManager().callEvent(new AsyncBankUpdateEvent(senderBank, Utils.loadAccounts(senderBank.getAccountNumbers(), cache, accountFetcher)));
        Bukkit.getPluginManager().callEvent(new AsyncBankUpdateEvent(recipientBank, Utils.loadAccounts(recipientBank.getAccountNumbers(), cache, accountFetcher)));
        Bukkit.getPluginManager().callEvent(new AsyncTransactionEvent(validated, recipient, sender));


        return (Transfer) validated;
    }

    @Override
    public Interest makeInterest(Account recipient, double interestRate) {
        Interest interest = factory.createInterest(recipient.getAccountNumber(), 0, interestRate);
        Validate.isTrue(recipient.getAccountNumber() == interest.getRecipientAccountNumber(), "The given recipient managers does not correspond with the transaction recipient managers-number");

        ((AccountImpl) recipient).setBalance(recipient.getBalance() + (recipient.getBalance() * interest.getInterestRate()));

        Transaction validated = transactionFetcher.saveData(interest);

        ((AccountImpl) recipient).addTransaction(validated);
        accountFetcher.saveData(recipient);

        Bank bank = Utils.loadBank(recipient.getOwner(), cache, bankFetcher);
        Bukkit.getPluginManager().callEvent(new AsyncBankUpdateEvent(bank, Utils.loadAccounts(bank.getAccountNumbers(), cache, accountFetcher)));
        Bukkit.getPluginManager().callEvent(new AsyncTransactionEvent(validated, recipient));

        return (Interest) validated;
    }

    @Override
    public Transaction makeDeposit(Account recipient, double amount) {
        Transaction transaction = factory.createDeposit(recipient.getAccountNumber(), amount);
        Validate.isTrue(recipient.getAccountNumber() == transaction.getRecipientAccountNumber(), "The given recipient managers does not correspond with the transaction recipient managers-number");

        Bank bank = Utils.loadBank(recipient.getOwner(), cache, bankFetcher);

        if(bank.getMoney() - amount < 0) throw new IllegalStateException("The bank has not enough money!");

        ((AccountImpl) recipient).setBalance(recipient.getBalance() + transaction.getAmount());
        ((BankImpl) bank).setMoney(bank.getMoney() - amount);

        Transaction validated = transactionFetcher.saveData(transaction);

        ((AccountImpl) recipient).addTransaction(validated);
        accountFetcher.saveData(recipient);
        bankFetcher.saveData(bank);

        Bukkit.getPluginManager().callEvent(new AsyncBankUpdateEvent(bank, Utils.loadAccounts(bank.getAccountNumbers(), cache, accountFetcher)));
        Bukkit.getPluginManager().callEvent(new AsyncTransactionEvent(validated, recipient));

        return validated;
    }

    @Override
    public Transaction makeWithdrawal(Account recipient, double amount) {
        Transaction transaction = factory.createWithdrawal(recipient.getAccountNumber(), amount);
        Validate.isTrue(recipient.getAccountNumber() == transaction.getRecipientAccountNumber(), "The given recipient managers does not correspond with the transaction recipient managers-number");

        if(recipient.getBalance() - transaction.getAmount() < 0) ((AccountImpl) recipient).setBalance(0);
        else ((AccountImpl) recipient).setBalance(recipient.getBalance() - transaction.getAmount());

        Bank bank = Utils.loadBank(recipient.getOwner(), cache, bankFetcher);
        ((BankImpl) bank).setMoney(bank.getMoney() + transaction.getAmount());

        Transaction validated = transactionFetcher.saveData(transaction);

        ((AccountImpl) recipient).addTransaction(validated);
        accountFetcher.saveData(recipient);
        bankFetcher.saveData(bank);

        Bukkit.getPluginManager().callEvent(new AsyncBankUpdateEvent(bank, Utils.loadAccounts(bank.getAccountNumbers(), cache, accountFetcher)));
        Bukkit.getPluginManager().callEvent(new AsyncTransactionEvent(validated, recipient));

        return validated;
    }

    @Override
    public Optional<Transaction> loadTransaction(int id) {
        Transaction transaction = cache.getTransactionCache().get(id);
        if(transaction == null) transaction = transactionFetcher.fetchData(id);
        return Optional.of(transaction);
    }

    @Override
    public Set<Transaction> loadTransactions(Set<Integer> ids) {
        Set<Transaction> transactions = new HashSet<>();
        Set<Integer> toFetch = new HashSet<>();

        for (int id : ids) {
            Transaction transaction = cache.getTransactionCache().get(id);
            if(transaction == null) {
                toFetch.add(id);
            } else {
                transactions.add(transaction);
            }
        }

        List<Transaction> fetched = new ArrayList<>();
        if(!toFetch.isEmpty()) fetched.addAll(transactionFetcher.fetchMultipleData(toFetch));
        for (Transaction transaction : fetched) {
            cache.getTransactionCache().add(transaction.getID(), transaction);
        }
        transactions.addAll(fetched);

        return transactions;
    }

    @Override
    public void deleteTransaction(int id) {
        cache.getTransactionCache().remove(id);
        transactionFetcher.deleteData(id);
    }

    @Override
    public void deleteTransaction(Transaction transaction) {
        deleteTransaction(transaction.getID());
    }

}
