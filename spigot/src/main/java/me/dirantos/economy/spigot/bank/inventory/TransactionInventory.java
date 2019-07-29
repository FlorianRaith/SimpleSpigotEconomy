package me.dirantos.economy.spigot.bank.inventory;

import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.transaction.Transaction;
import me.dirantos.economy.spigot.inventory.InventoryBackground;
import me.dirantos.economy.spigot.inventory.InventoryManager;
import me.dirantos.economy.spigot.inventory.InventoryPage;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TransactionInventory extends InventoryPage {

    private final Account account;
    private final Set<Transaction> transactions;
    private final BankInventory bankInventory;
    private final InventoryManager inventoryManager;
    private final Player player;

    public TransactionInventory(Account account, Set<Transaction> transactions, BankInventory bankInventory, InventoryManager inventoryManager, Player player) {
        super(4, "Transactions - " + account.getAccountNumber(), InventoryBackground.BLACK);
        this.account = account;
        this.transactions = transactions;
        this.bankInventory = bankInventory;
        this.inventoryManager = inventoryManager;
        this.player = player;
    }

    @Override
    public void build() {
        int slot = 0;
        List<Transaction> sorted = transactions.stream().sorted(Comparator.comparingInt(Transaction::getID)).collect(Collectors.toList());
        Collections.reverse(sorted);
        for (Transaction transaction : sorted) {
            if(slot >= 10) break;
            registerItem(slot, new TransactionItem(transaction, account));
            slot++;
        }
        registerItem((9*getHeight())-1, new ReturnItem(bankInventory, inventoryManager, player));
    }

}
