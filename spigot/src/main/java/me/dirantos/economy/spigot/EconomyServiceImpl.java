package me.dirantos.economy.spigot;

import me.dirantos.economy.api.account.AccountFetcher;
import me.dirantos.economy.api.bank.BankFetcher;
import me.dirantos.economy.api.transaction.TransactionFetcher;
import me.dirantos.economy.api.account.AccountManager;
import me.dirantos.economy.api.bank.BankManager;
import me.dirantos.economy.api.transaction.TransactionManager;
import me.dirantos.economy.api.EconomyService;

public final class EconomyServiceImpl implements EconomyService {

    private final AccountFetcher accountFetcher;
    private final BankFetcher bankFetcher;
    private final TransactionFetcher transactionFetcher;
    private final TransactionManager transactionManager;
    private final AccountManager accountManager;
    private final BankManager bankManager;

    public EconomyServiceImpl(AccountFetcher accountFetcher, BankFetcher bankFetcher, TransactionFetcher transactionFetcher, TransactionManager transactionManager, AccountManager accountManager, BankManager bankManager) {
        this.accountFetcher = accountFetcher;
        this.bankFetcher = bankFetcher;
        this.transactionFetcher = transactionFetcher;
        this.transactionManager = transactionManager;
        this.accountManager = accountManager;
        this.bankManager = bankManager;
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

}
