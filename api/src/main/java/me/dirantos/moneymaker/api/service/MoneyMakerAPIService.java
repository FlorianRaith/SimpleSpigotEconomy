package me.dirantos.moneymaker.api.service;

import me.dirantos.moneymaker.api.managers.AccountManager;
import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.fetchers.BankFetcher;
import me.dirantos.moneymaker.api.fetchers.TransactionFetcher;
import me.dirantos.moneymaker.api.managers.BankManager;
import me.dirantos.moneymaker.api.managers.TransactionManager;
import me.dirantos.moneymaker.api.cache.ModelCache;

public interface MoneyMakerAPIService {

    /**
     * The AccountFetcher gives you direct access to the account-table in the database
     * but it is recommended to use the AccountManager for any account-operations
     * @return account-fetcher
     */
    AccountFetcher getAccountFetcher();

    /**
     * The BankFetcher gives you direct access to the bank-table in the database
     * but it is recommended to use the BankManager for any bank-operations
     * @return bank-fetcher
     */
    BankFetcher getBankFetcher();

    /**
     * The TransactionFetcher gives you direct access to the transaction-table in the database
     * but it is recommended to use the TransactionManager for any transaction-operations
     * @return transaction-fetcher
     */
    TransactionFetcher getTransactionFetcher();

    TransactionManager getTransactionManager();

    AccountManager getAccountManager();

    BankManager getBankManager();

    ModelCache getCache();

}