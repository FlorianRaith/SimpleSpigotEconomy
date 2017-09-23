package me.dirantos.moneymaker.spigot;

import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.fetchers.BankFetcher;
import me.dirantos.moneymaker.api.fetchers.TransactionFetcher;
import me.dirantos.moneymaker.api.service.MoneyMakerAPIService;

public final class MoneyMakerService implements MoneyMakerAPIService {

    private final AccountFetcher accountFetcher;
    private final BankFetcher bankFetcher;
    private final TransactionFetcher transactionFetcher;

    public MoneyMakerService(AccountFetcher accountFetcher, BankFetcher bankFetcher, TransactionFetcher transactionFetcher) {
        this.accountFetcher = accountFetcher;
        this.bankFetcher = bankFetcher;
        this.transactionFetcher = transactionFetcher;
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

}
