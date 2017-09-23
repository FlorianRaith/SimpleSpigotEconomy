package me.dirantos.moneymaker.spigot.account;

import me.dirantos.moneymaker.api.account.AccountManager;
import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.spigot.models.AccountImpl;

import java.util.ArrayList;
import java.util.UUID;

public final class AccountManagerImpl implements AccountManager {

    private final AccountFetcher accountFetcher;

    public AccountManagerImpl(AccountFetcher accountFetcher) {
        this.accountFetcher = accountFetcher;
    }

    @Override
    public Account createNewAccount(UUID owner, double startBalance) {
        return accountFetcher.saveData(new AccountImpl(-1, owner, startBalance, new ArrayList<>()));
    }

    @Override
    public Account createNewAccount(UUID owner) {
        return createNewAccount(owner, 0);
    }

    @Override
    public void deleteAccount(Account account) {
        accountFetcher.deleteData(account.getAccountNumber());
    }

    @Override
    public void deleteAccount(int accountNumber) {
        accountFetcher.deleteData(accountNumber);
    }

}
