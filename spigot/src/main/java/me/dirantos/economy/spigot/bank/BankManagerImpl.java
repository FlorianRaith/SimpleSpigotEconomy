package me.dirantos.economy.spigot.bank;

import me.dirantos.economy.spigot.ModelCache;
import me.dirantos.economy.api.bank.BankFetcher;
import me.dirantos.economy.api.account.AccountManager;
import me.dirantos.economy.api.bank.BankManager;
import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.bank.Bank;
import me.dirantos.economy.api.bank.AsyncBankUpdateEvent;
import me.dirantos.economy.spigot.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
        return accountManager.loadAccounts(new HashSet<>(bank.getAccountNumbers()));
    }

    @Override
    public void deleteBank(Bank bank) {
        cache.getBankCache().remove(bank.getOwner());
        bankFetcher.deleteData(bank.getOwner());
    }

}
