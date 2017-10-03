package me.dirantos.moneymaker.spigot.bank;

import me.dirantos.moneymaker.api.models.Account;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;

public class AsyncBankInventoryOpenEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Set<Account> accounts;
    private final Player player;

    public AsyncBankInventoryOpenEvent(Set<Account> accounts, Player player) {
        super(true);
        this.accounts = accounts;
        this.player = player;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public Player getPlayer() {
        return player;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }


}
