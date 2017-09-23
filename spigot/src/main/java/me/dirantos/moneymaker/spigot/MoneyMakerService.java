package me.dirantos.moneymaker.spigot;

import me.dirantos.moneymaker.api.account.AccountManager;
import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.fetchers.BankFetcher;
import me.dirantos.moneymaker.api.fetchers.TransactionFetcher;
import me.dirantos.moneymaker.api.service.MoneyMakerAPIService;
import me.dirantos.moneymaker.api.transaction.TransactionManager;

public final class MoneyMakerService implements MoneyMakerAPIService {

    private final AccountFetcher accountFetcher;
    private final BankFetcher bankFetcher;
    private final TransactionFetcher transactionFetcher;
    private final TransactionManager transactionManager;
    private final AccountManager accountManager;

    public MoneyMakerService(AccountFetcher accountFetcher, BankFetcher bankFetcher, TransactionFetcher transactionFetcher, TransactionManager transactionManager, AccountManager accountManager) {
        this.accountFetcher = accountFetcher;
        this.bankFetcher = bankFetcher;
        this.transactionFetcher = transactionFetcher;
        this.transactionManager = transactionManager;
        this.accountManager = accountManager;
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

}
