package me.dirantos.moneymaker.api.service;

import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.fetchers.BankFetcher;
import me.dirantos.moneymaker.api.fetchers.TransactionFetcher;
import me.dirantos.moneymaker.api.transaction.TransactionManager;

public interface MoneyMakerAPIService {

    AccountFetcher getAccountFetcher();

    BankFetcher getBankFetcher();

    TransactionFetcher getTransactionFetcher();

    TransactionManager getTransactionManager();

}