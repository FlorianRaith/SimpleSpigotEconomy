package me.dirantos.economy.spigot.managers;

import me.dirantos.economy.api.cache.ModelCache;
import me.dirantos.economy.api.fetchers.BankFetcher;
import me.dirantos.economy.api.managers.AccountManager;
import me.dirantos.economy.api.managers.BankManager;
import me.dirantos.economy.api.models.Account;
import me.dirantos.economy.api.models.Bank;
import me.dirantos.economy.api.events.AsyncBankUpdateEvent;
import me.dirantos.economy.spigot.models.BankImpl;
import me.dirantos.economy.spigot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
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
        return Utils.loadBank(owner, cache, bankFetcher);
    }

    @Override
    public Bank loadBank(Player player) {
        return loadBank(player.getUniqueId());
    }

    @Override
    public void setBalance(Bank bank, double amount) {
        ((BankImpl) bank).setMoney(amount);
        bankFetcher.saveData(bank);
        Set<Account> accounts = loadAccounts(bank);
        Bukkit.getPluginManager().callEvent(new AsyncBankUpdateEvent(bank, accounts));
    }

    @Override
    public Set<Account> loadAccounts(Bank bank) {
        if(bank.getAccountNumbers().isEmpty()) return new HashSet<>();
        return accountManager.loadAccounts(bank.getAccountNumbers().stream().collect(Collectors.toSet()));
    }

    @Override
    public void deleteBank(Bank bank) {
        cache.getBankCache().remove(bank.getOwner());
        bankFetcher.deleteData(bank.getOwner());
    }

}
