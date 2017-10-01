package me.dirantos.moneymaker.api.events;

import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.models.Bank;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;

public class AsyncBankUpdateEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Bank bank;
    private final Set<Account> accounts;

    public AsyncBankUpdateEvent(Bank bank, Set<Account> accounts) {
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
