package me.dirantos.economy.api.transaction;

import me.dirantos.economy.api.account.Account;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Optional;

/**
 * This events get called when a transaction to a specific account will be made
 */
public class AsyncTransactionEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Transaction transaction;
    private final Account account;
    private final Account recipient;
    private final Account sender;

    public AsyncTransactionEvent(Transaction transaction, Account account, Account recipient, Account sender) {
        super(true);
        this.transaction = transaction;
        this.account = account;
        this.recipient = recipient;
        this.sender = sender;
    }

    public AsyncTransactionEvent(Transaction transaction, Account account) {
        this(transaction, account, null, null);
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Account getAccount() {
        return account;
    }

    public Optional<Account> getRecipient() {
        return Optional.ofNullable(recipient);
    }

    public Optional<Account> getSender() {
        return Optional.ofNullable(sender);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
