package me.dirantos.economy.api.bank;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event gets called when a bank got created and saved into the database
 */
public class AsyncBankCreateEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Bank bank;

    public AsyncBankCreateEvent(Bank bank) {
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
