package me.dirantos.economy.api.bank;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event get called when the wallet balances changes
 */
public class AsyncBankUpdateEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Bank bank;
    private final double oldWalletBalance;
    private final double newWalletBalance;

    public AsyncBankUpdateEvent(Bank bank, double oldWalletBalance, double newWalletBalance) {
        super(true);
        this.bank = bank;
        this.oldWalletBalance = oldWalletBalance;
        this.newWalletBalance = newWalletBalance;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Bank getBank() {
        return bank;
    }

    public double getOldWalletBalance() {
        return oldWalletBalance;
    }

    public double getNewWalletBalance() {
        return newWalletBalance;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
