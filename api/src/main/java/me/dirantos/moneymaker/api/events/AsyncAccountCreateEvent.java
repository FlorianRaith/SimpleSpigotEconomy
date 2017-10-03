package me.dirantos.moneymaker.api.events;

import me.dirantos.moneymaker.api.models.Account;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event gets called when an account gets created
 */
public class AsyncAccountCreateEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Account account;

    public AsyncAccountCreateEvent(Account account) {
        super(true);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
