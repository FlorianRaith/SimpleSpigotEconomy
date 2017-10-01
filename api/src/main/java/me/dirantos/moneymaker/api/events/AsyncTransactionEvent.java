package me.dirantos.moneymaker.api.events;

import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.models.Transaction;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AsyncTransactionEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Transaction transaction;
    private final Account recipient;
    private final Account sender;

    public AsyncTransactionEvent(Transaction transaction, Account recipient, Account sender) {
        super(true);
        this.transaction = transaction;
        this.recipient = recipient;
        this.sender = sender;
    }

    public AsyncTransactionEvent(Transaction transaction, Account recipient) {
        this(transaction, recipient, null);
    }


    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Account getRecipient() {
        return recipient;
    }

    public Account getSender() {
        return sender;
    }

    public boolean hasSender() {
        return sender != null;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
