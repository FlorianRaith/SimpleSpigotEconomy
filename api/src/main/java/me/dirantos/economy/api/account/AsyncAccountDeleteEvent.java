package me.dirantos.economy.api.account;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event gets called when an account gets deleted
 */
public class AsyncAccountDeleteEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final int accountID;

    public AsyncAccountDeleteEvent(int accountID) {
        super(true);
        this.accountID = accountID;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public int getAccountID() {
        return accountID;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
