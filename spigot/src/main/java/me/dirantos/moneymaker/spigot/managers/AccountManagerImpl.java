package me.dirantos.moneymaker.spigot.managers;

import me.dirantos.moneymaker.api.managers.AccountManager;
import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.models.Transaction;
import me.dirantos.moneymaker.api.managers.TransactionManager;
import me.dirantos.moneymaker.spigot.models.AccountImpl;
import me.dirantos.moneymaker.api.cache.ModelCache;

import java.util.*;
import java.util.stream.Collectors;

public final class AccountManagerImpl implements AccountManager {

    private final AccountFetcher accountFetcher;
    private final TransactionManager transactionManager;
    private final ModelCache cache;

    public AccountManagerImpl(AccountFetcher accountFetcher, TransactionManager transactionManager, ModelCache cache) {
        this.accountFetcher = accountFetcher;
        this.transactionManager = transactionManager;
        this.cache = cache;
    }

    @Override
    public Account createNewAccount(UUID owner, double startBalance) {
        Account account = accountFetcher.saveData(new AccountImpl(-1, owner, startBalance, new ArrayList<>()));
        cache.getAccountCache().add(account.getAccountNumber(), account);
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

        accounts.addAll(accountFetcher.fetchMultipleData(toFetch));

        return accounts;
    }

    @Override
    public Optional<Account> loadAccount(Integer accountNumber) {
        Account account = cache.getAccountCache().get(accountNumber);
        if(account == null) account = accountFetcher.fetchData(accountNumber);
        return Optional.of(account);
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
    }

    @Override
    public void deleteAccount(int accountNumber) {
        accountFetcher.deleteData(accountNumber);
    }

}
