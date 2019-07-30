package me.dirantos.economy.api.account;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event gets called when the account balance changes
 */
public class AsyncAccountUpdateEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Account account;
    private final double oldAmount;
    private final double newAmount;

    public AsyncAccountUpdateEvent(Account account, double oldAmount, double newAmount) {
        super(true);
        this.account = account;
        this.oldAmount = oldAmount;
        this.newAmount = newAmount;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Account getAccount() {
        return account;
    }

    public double getOldAmount() {
        return oldAmount;
    }

    public double getNewAmount() {
        return newAmount;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
