package me.dirantos.economy.spigot.managers;

import me.dirantos.economy.api.cache.ModelCache;
import me.dirantos.economy.api.events.AsyncAccountCreateEvent;
import me.dirantos.economy.api.events.AsyncAccountDeleteEvent;
import me.dirantos.economy.api.events.AsyncBankUpdateEvent;
import me.dirantos.economy.api.fetchers.AccountFetcher;
import me.dirantos.economy.api.fetchers.BankFetcher;
import me.dirantos.economy.api.managers.AccountManager;
import me.dirantos.economy.api.managers.TransactionManager;
import me.dirantos.economy.api.models.Account;
import me.dirantos.economy.api.models.Bank;
import me.dirantos.economy.api.models.Transaction;
import me.dirantos.economy.spigot.models.AccountImpl;
import me.dirantos.economy.spigot.models.BankImpl;
import me.dirantos.economy.spigot.utils.Utils;
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
