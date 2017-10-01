package me.dirantos.moneymaker.spigot.bankupdate;

import me.dirantos.moneymaker.api.cache.ModelCache;
import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.fetchers.BankFetcher;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.models.Bank;
import me.dirantos.moneymaker.spigot.models.BankImpl;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.*;

public class BankUpdateEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Bank bank;
    private final Set<Account> accounts;

    public BankUpdateEvent(Bank bank, Set<Account> accounts) {
        super(true);
        this.bank = bank;
        this.accounts = accounts;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Bank getBank() {
        return bank;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
