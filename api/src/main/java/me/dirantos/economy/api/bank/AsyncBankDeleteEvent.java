package me.dirantos.economy.api.bank;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class AsyncBankDeleteEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final UUID owner;

    public AsyncBankDeleteEvent(UUID bank) {
        super(true);
        this.owner = bank;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public UUID getOwner() {
        return owner;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
