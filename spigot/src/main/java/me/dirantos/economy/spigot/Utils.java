package me.dirantos.economy.spigot;

import me.dirantos.economy.spigot.ModelCache;
import me.dirantos.economy.api.account.AccountFetcher;
import me.dirantos.economy.api.bank.BankFetcher;
import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.bank.Bank;
import me.dirantos.economy.spigot.bank.BankImpl;

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

    public static String formatMoney(double money) {
        return String.format(Locale.ENGLISH, "%.2f", money) + "$";
    }

}
