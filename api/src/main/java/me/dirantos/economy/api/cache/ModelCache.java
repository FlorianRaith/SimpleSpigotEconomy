package me.dirantos.economy.api.cache;

import me.dirantos.economy.api.models.Account;
import me.dirantos.economy.api.models.Bank;
import me.dirantos.economy.api.models.Transaction;

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
