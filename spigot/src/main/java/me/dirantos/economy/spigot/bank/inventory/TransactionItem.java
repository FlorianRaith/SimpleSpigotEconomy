package me.dirantos.economy.spigot.bank.inventory;

import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.transaction.Interest;
import me.dirantos.economy.api.transaction.Transaction;
import me.dirantos.economy.api.transaction.TransactionType;
import me.dirantos.economy.api.transaction.Transfer;
import me.dirantos.economy.spigot.inventory.InventoryItem;
import me.dirantos.economy.spigot.inventory.ItemFactory;
import me.dirantos.economy.spigot.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransactionItem extends InventoryItem {


    private static final String KEY = ChatColor.RESET + "" + ChatColor.GRAY;
    private static final String VALUE = ChatColor.YELLOW + "";

    private final Transaction transaction;
    private final Account account;

    public TransactionItem(Transaction transaction, Account account) {
        this.transaction = transaction;
        this.account = account;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public ItemStack createItem() {
        short data;

        if(transaction.getType() == TransactionType.DEPOSIT) {
            data = (short) 5;
        } else if(transaction.getType() == TransactionType.TRANSFER && transaction instanceof Transfer) {
            Transfer transfer = (Transfer) transaction;
            if(transfer.getRecipientAccountID() == transfer.getAccountID()) {
                data = (short) 5;
            } else {
                data = (short) 14;
            }
        } else {
            data = (short) 14;
        }

        String name = ChatColor.RESET + "" + ChatColor.BOLD + (data == 5 ? ChatColor.GREEN : ChatColor.RED) +
                transaction.getType().toString().substring(0,1) + transaction.getType().toString().substring(1).toLowerCase();

        ItemStack item = ItemFactory.create(name, Material.WOOL, data);

        List<String> lore = new ArrayList<>(Arrays.asList(
                KEY + "id: " + VALUE + transaction.getID(),
                KEY + "recipient: " + VALUE + transaction.getAccountID()
        ));
        if(transaction instanceof Transfer) lore.add(KEY + "sender: " + VALUE + ((Transfer) transaction).getSenderAccountID());
        lore.add(KEY + "amount: " + VALUE + Utils.formatMoney(transaction.getAmount()));
        if(transaction instanceof Interest) lore.add(KEY + "interestRate: " + VALUE + ((Interest) transaction).getInterestRate()*100 + "%");
        lore.add(KEY + "date: " + VALUE + transaction.getDate().toString());

        return ItemFactory.setLore(item, lore);
    }

}
