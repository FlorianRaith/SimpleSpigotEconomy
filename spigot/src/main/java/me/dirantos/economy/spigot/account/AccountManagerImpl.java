package me.dirantos.economy.spigot.account;

import me.dirantos.economy.spigot.bank.BankImpl;
import me.dirantos.economy.spigot.ModelCache;
import me.dirantos.economy.api.account.AsyncAccountCreateEvent;
import me.dirantos.economy.api.account.AsyncAccountDeleteEvent;
import me.dirantos.economy.api.bank.AsyncBankUpdateEvent;
import me.dirantos.economy.api.account.AccountFetcher;
import me.dirantos.economy.api.bank.BankFetcher;
import me.dirantos.economy.api.account.AccountManager;
import me.dirantos.economy.api.transaction.TransactionManager;
import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.bank.Bank;
import me.dirantos.economy.api.transaction.Transaction;
import me.dirantos.economy.spigot.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.*;

public final class AccountManagerImpl implements AccountManager {

    private final AccountFetcher accountFetcher;
    private final TransactionManager transactionManager;
    private final BankFetcher bankFetcher;
    private final ModelCache cache;

    public AccountManagerImpl(Plugin plugin, AccountFetcher accountFetcher, TransactionManager transactionManager, BankFetcher bankFetcher, ModelCache cache) {
        this.accountFetcher = accountFetcher;
        this.transactionManager = transactionManager;
        this.bankFetcher = bankFetcher;
        this.cache = cache;
    }

    @Override
    public Account createNewAccount(UUID owner, double startBalance) {
        Account account = accountFetcher.saveData(new AccountImpl(-1, owner, startBalance, new HashSet<>()));
        cache.getAccountCache().add(account.getAccountNumber(), account);
        Bank bank = Utils.loadBank(owner, cache, bankFetcher);
        ((BankImpl) bank).addAccount(account.getAccountNumber());
        bankFetcher.saveData(bank);

        Bukkit.getPluginManager().callEvent(new AsyncBankUpdateEvent(bank, Utils.loadAccounts(bank.getAccountNumbers(), cache, accountFetcher)));
        Bukkit.getPluginManager().callEvent(new AsyncAccountCreateEvent(account));
        return account;
    }

    @Override
    public Set<Account> loadAccounts(Set<Integer> accountNumbers) {
        Set<Account> accounts = new HashSet<>();
        Set<Integer> toFetch = new HashSet<>();

        for (int accountNumber : accountNumbers) {
            Account account = cache.getAccountCache().get(accountNumber);
            if(account == null) {
                toFetch.add(accountNumber);
            } else {
                accounts.add(account);
            }
        }

        List<Account> fetched = new ArrayList<>();
        if(!toFetch.isEmpty()) fetched.addAll(accountFetcher.fetchMultipleData(toFetch));
        for (Account account : fetched) {
            cache.getAccountCache().add(account.getAccountNumber(), account);
        }
        accounts.addAll(fetched);

        return accounts;
    }

    @Override
    public Optional<Account> loadAccount(Integer accountNumber) {
        Account account = cache.getAccountCache().get(accountNumber);
        if(account == null) account = accountFetcher.fetchData(accountNumber);
        return Optional.ofNullable(account);
    }

    @Override
    public Set<Transaction> loadAllTransactions(Account account) {
        return transactionManager.loadTransactions(account.getTransactionIDs());
    }

    @Override
    public Account createNewAccount(UUID owner) {
        return createNewAccount(owner, 0);
    }

    @Override
    public void deleteAccount(Account account) {
        cache.getAccountCache().remove(account.getAccountNumber());
        accountFetcher.deleteData(account.getAccountNumber());
        Bank bank = Utils.loadBank(account.getOwner(), cache, bankFetcher);
        ((BankImpl) bank).removeAccount(account.getAccountNumber());
        bankFetcher.saveData(bank);
        Bukkit.getPluginManager().callEvent(new AsyncBankUpdateEvent(bank, Utils.loadAccounts(bank.getAccountNumbers(), cache, accountFetcher)));
        Bukkit.getPluginManager().callEvent(new AsyncAccountDeleteEvent(account));
    }

    @Override
    public void deleteAccount(int accountNumber) {
        accountFetcher.deleteData(accountNumber);
    }

}
