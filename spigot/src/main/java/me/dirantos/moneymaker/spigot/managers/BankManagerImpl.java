package me.dirantos.moneymaker.spigot.managers;

import me.dirantos.moneymaker.api.cache.ModelCache;
import me.dirantos.moneymaker.api.fetchers.BankFetcher;
import me.dirantos.moneymaker.api.managers.AccountManager;
import me.dirantos.moneymaker.api.managers.BankManager;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.models.Bank;
import me.dirantos.moneymaker.spigot.models.BankImpl;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankManagerImpl implements BankManager {

    private final BankFetcher bankFetcher;
    private final AccountManager accountManager;
    private final ModelCache cache;

    public BankManagerImpl(BankFetcher bankFetcher, AccountManager accountManager, ModelCache cache) {
        this.bankFetcher = bankFetcher;
        this.accountManager = accountManager;
        this.cache = cache;
    }

    @Override
    public Bank loadBank(UUID owner) {
        Bank bank = cache.getBankCache().get(owner);
        if(bank == null) bankFetcher.fetchData(owner);
        if(bank == null) {
            bank = new BankImpl(owner, new ArrayList<>(), 0);
            bankFetcher.saveData(bank);
            cache.getBankCache().add(owner, bank);
        }
        return bank;
    }

    @Override
    public Bank loadBank(Player player) {
        return loadBank(player.getUniqueId());
    }

    @Override
    public void setBalance(Bank bank, double amount) {
        bank.setMoney(amount);
        bankFetcher.saveData(bank);
    }

    @Override
    public Set<Account> loadAccounts(Bank bank) {
        return accountManager.loadAccounts(bank.getAccountNumbers().stream().collect(Collectors.toSet()));
    }

    @Override
    public void deleteBank(Bank bank) {
        cache.getBankCache().remove(bank.getOwner());
        bankFetcher.deleteData(bank.getOwner());
    }

}