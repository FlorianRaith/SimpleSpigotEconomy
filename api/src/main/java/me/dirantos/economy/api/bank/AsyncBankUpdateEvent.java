package me.dirantos.economy.api.bank;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This events get called when the bank balances changes,
 * when one of the accounts balance changes and when an account gets created or gets deleted
 */
public class AsyncBankUpdateEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Bank bank;

    public AsyncBankUpdateEvent(Bank bank) {
        super(true);
        this.bank = bank;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Bank getBank() {
        return bank;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
