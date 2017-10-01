package me.dirantos.moneymaker.spigot.utils;

import me.dirantos.moneymaker.api.cache.ModelCache;
import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.fetchers.BankFetcher;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.models.Bank;
import me.dirantos.moneymaker.spigot.models.BankImpl;

import java.util.*;

public final class Utils {

    private Utils() {}

    public static Bank loadBank(UUID uuid, ModelCache cache, BankFetcher bankFetcher) {
        Bank bank = cache.getBankCache().get(uuid);
        if(bank == null) bank = bankFetcher.fetchData(uuid);
        if(bank == null) {
            bank = new BankImpl(uuid, new ArrayList<>(), 0);
            bankFetcher.saveData(bank);
            cache.getBankCache().add(uuid, bank);
        }
        return bank;
    }

    public static Set<Account> loadAccounts(List<Integer> accountNumbers, ModelCache cache, AccountFetcher accountFetcher) {
        Set<Account> accounts = new HashSet<>();
        Set<Integer> toFetch = new HashSet<>();

        for (int accountNumber : accountNumbers) {
            Account account = cache.getAccountCache().get(accountNumber);
            if(account == null) {
                toFetch.add(accountNumber);
            } else {
                accounts.add(account);
            }
        }

        List<Account> fetched = new ArrayList<>();
        if(!toFetch.isEmpty()) fetched.addAll(accountFetcher.fetchMultipleData(toFetch));
        for (Account account : fetched) {
            cache.getAccountCache().add(account.getAccountNumber(), account);
        }
        accounts.addAll(fetched);
        return accounts;
    }

}
