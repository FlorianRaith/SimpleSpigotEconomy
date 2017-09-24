package me.dirantos.moneymaker.spigot.account;

import me.dirantos.moneymaker.api.account.AccountManager;
import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.spigot.models.AccountImpl;
import me.dirantos.moneymaker.api.utils.ModelCache;

import java.util.ArrayList;
import java.util.UUID;

public final class AccountManagerImpl implements AccountManager {

    private final AccountFetcher accountFetcher;
    private final ModelCache cache;

    public AccountManagerImpl(AccountFetcher accountFetcher, ModelCache cache) {
        this.accountFetcher = accountFetcher;
        this.cache = cache;
    }

    @Override
    public Account createNewAccount(UUID owner, double startBalance) {
        Account account = accountFetcher.saveData(new AccountImpl(-1, owner, startBalance, new ArrayList<>()));
        cache.getAccountCache().add(account.getAccountNumber(), account);
        return account;
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
