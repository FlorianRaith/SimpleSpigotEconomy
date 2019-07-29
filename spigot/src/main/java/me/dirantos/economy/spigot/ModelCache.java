package me.dirantos.economy.spigot;

import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.bank.Bank;
import me.dirantos.economy.api.transaction.Transaction;
import me.dirantos.economy.spigot.Cache;

import java.util.UUID;

public class ModelCache {

    private final Cache<Account, Integer> accountCache = new Cache<>();
    private final Cache<Bank, UUID> bankCache = new Cache<>();
    private final Cache<Transaction, Integer> transactionCache = new Cache<>();

    public Cache<Account, Integer> getAccountCache() {
        return accountCache;
    }

    public Cache<Bank, UUID> getBankCache() {
        return bankCache;
    }

    public Cache<Transaction, Integer> getTransactionCache() {
        return transactionCache;
    }

}
