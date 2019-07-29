package me.dirantos.economy.spigot;

import me.dirantos.economy.api.cache.ModelCache;
import me.dirantos.economy.api.fetchers.AccountFetcher;
import me.dirantos.economy.api.fetchers.BankFetcher;
import me.dirantos.economy.api.fetchers.TransactionFetcher;
import me.dirantos.economy.api.managers.AccountManager;
import me.dirantos.economy.api.managers.BankManager;
import me.dirantos.economy.api.managers.TransactionManager;
import me.dirantos.economy.api.service.EconomyService;

public final class EconomyServiceImpl implements EconomyService {

    private final AccountFetcher accountFetcher;
    private final BankFetcher bankFetcher;
    private final TransactionFetcher transactionFetcher;
    private final TransactionManager transactionManager;
    private final AccountManager accountManager;
    private final BankManager bankManager;
    private final ModelCache cache;

    public EconomyServiceImpl(AccountFetcher accountFetcher, BankFetcher bankFetcher, TransactionFetcher transactionFetcher, TransactionManager transactionManager, AccountManager accountManager, BankManager bankManager, ModelCache cache) {
        this.accountFetcher = accountFetcher;
        this.bankFetcher = bankFetcher;
        this.transactionFetcher = transactionFetcher;
        this.transactionManager = transactionManager;
        this.accountManager = accountManager;
        this.bankManager = bankManager;
        this.cache = cache;
    }

    @Override
    public AccountFetcher getAccountFetcher() {
        return accountFetcher;
    }

    @Override
    public BankFetcher getBankFetcher() {
        return bankFetcher;
    }

    @Override
    public TransactionFetcher getTransactionFetcher() {
        return transactionFetcher;
    }

    @Override
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    @Override
    public AccountManager getAccountManager() {
        return accountManager;
    }

    @Override
    public BankManager getBankManager() {
        return bankManager;
    }

    @Override
    public ModelCache getCache() {
        return cache;
    }

}
