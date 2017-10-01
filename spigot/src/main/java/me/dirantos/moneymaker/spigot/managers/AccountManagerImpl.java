package me.dirantos.moneymaker.spigot.managers;

import me.dirantos.moneymaker.api.cache.ModelCache;
import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.fetchers.BankFetcher;
import me.dirantos.moneymaker.api.managers.AccountManager;
import me.dirantos.moneymaker.api.managers.TransactionManager;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.models.Bank;
import me.dirantos.moneymaker.api.models.Transaction;
import me.dirantos.moneymaker.spigot.models.AccountImpl;
import me.dirantos.moneymaker.spigot.models.BankImpl;
import me.dirantos.moneymaker.spigot.bankupdate.BankUpdateEvent;
import me.dirantos.moneymaker.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

public final class AccountManagerImpl implements AccountManager {

    private final AccountFetcher accountFetcher;
    private final TransactionManager transactionManager;
    private final BankFetcher bankFetcher;
    private final ModelCache cache;
    private final Plugin plugin;

    public AccountManagerImpl(Plugin plugin, AccountFetcher accountFetcher, TransactionManager transactionManager, BankFetcher bankFetcher, ModelCache cache) {
        this.plugin = plugin;
        this.accountFetcher = accountFetcher;
        this.transactionManager = transactionManager;
        this.bankFetcher = bankFetcher;
        this.cache = cache;
    }

    @Override
    public Account createNewAccount(UUID owner, double startBalance) {
        Account account = accountFetcher.saveData(new AccountImpl(-1, owner, startBalance, new ArrayList<>()));
        cache.getAccountCache().add(account.getAccountNumber(), account);
        Bank bank = cache.getBankCache().get(owner);
        if(bank == null) bank = bankFetcher.fetchData(owner);
        if(bank == null) {
            bank = new BankImpl(owner, new ArrayList<>(), 0);
            bankFetcher.saveData(bank);
            cache.getBankCache().add(owner, bank);
        }
        ((BankImpl) bank).addAccount(account.getAccountNumber());
        bankFetcher.saveData(bank);

        Set<Account> accounts = new HashSet<>();
        Set<Integer> toFetch = new HashSet<>();

        for (int accountNumber : bank.getAccountNumbers()) {
            Account acc = cache.getAccountCache().get(accountNumber);
            if(acc == null) {
                toFetch.add(accountNumber);
            } else {
                accounts.add(acc);
            }
        }

        List<Account> fetched = new ArrayList<>();
        if(!toFetch.isEmpty()) fetched.addAll(accountFetcher.fetchMultipleData(toFetch));
        for (Account acc : fetched) {
            cache.getAccountCache().add(acc.getAccountNumber(), acc);
        }
        accounts.addAll(fetched);

        Bukkit.getPluginManager().callEvent(new BankUpdateEvent(bank, accounts));
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
        return transactionManager.loadTransactions(account.getTransactionIDs().stream().collect(Collectors.toSet()));
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
        Bukkit.getPluginManager().callEvent(new BankUpdateEvent(bank, Utils.loadAccounts(bank.getAccountNumbers(), cache, accountFetcher)));
    }

    @Override
    public void deleteAccount(int accountNumber) {
        accountFetcher.deleteData(accountNumber);
    }

}
