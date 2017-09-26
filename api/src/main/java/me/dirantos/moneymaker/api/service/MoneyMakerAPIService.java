package me.dirantos.moneymaker.api.service;

import me.dirantos.moneymaker.api.managers.AccountManager;
import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.fetchers.BankFetcher;
import me.dirantos.moneymaker.api.fetchers.TransactionFetcher;
import me.dirantos.moneymaker.api.managers.BankManager;
import me.dirantos.moneymaker.api.managers.TransactionManager;
import me.dirantos.moneymaker.api.cache.ModelCache;

public interface MoneyMakerAPIService {

    AccountFetcher getAccountFetcher();

    BankFetcher getBankFetcher();

    TransactionFetcher getTransactionFetcher();

    TransactionManager getTransactionManager();

    AccountManager getAccountManager();

    BankManager getBankManager();

    ModelCache getCache();

}