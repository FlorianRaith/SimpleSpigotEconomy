package me.dirantos.economy.api;

import me.dirantos.economy.api.account.AccountFetcher;
import me.dirantos.economy.api.bank.BankFetcher;
import me.dirantos.economy.api.account.AccountManager;
import me.dirantos.economy.api.transaction.TransactionFetcher;
import me.dirantos.economy.api.bank.BankManager;
import me.dirantos.economy.api.transaction.TransactionManager;

public interface EconomyService {

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

    /**
     * The TransactionManagers provides different methods to handle transactions
     * All methods should be called asynchronously because they are working directly with the database
     * @return TransactionManager
     */
    TransactionManager getTransactionManager();

    /**
     * The AccountManager provides different methods to handle accounts
     * All methods should be called asynchronously because they are working directly with the database
     * @return AccountManager
     */
    AccountManager getAccountManager();

    /**
     * The BankManager provides different methods to handle banks
     * All methods should be called asynchronously because they are working directly with the database
     * @return BankManager
     */
    BankManager getBankManager();

}