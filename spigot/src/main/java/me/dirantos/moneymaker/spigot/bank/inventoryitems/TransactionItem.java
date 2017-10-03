package me.dirantos.moneymaker.spigot.bank.inventoryitems;

import me.dirantos.moneymaker.api.models.*;
import me.dirantos.moneymaker.components.inventory.InventoryItem;
import me.dirantos.moneymaker.components.inventory.ItemFactory;
import me.dirantos.moneymaker.spigot.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
        short data = transaction.getType() == TransactionType.DEPOSIT ||
                (transaction instanceof Transfer && transaction.getRecipientAccountNumber() == account.getAccountNumber()) ||
                transaction instanceof Interest ? (short) 5 : (short) 14;

        String name = ChatColor.RESET + "" + ChatColor.BOLD + (data == 5 ? ChatColor.GREEN : ChatColor.RED) +
                transaction.getType().toString().substring(0,1) + transaction.getType().toString().substring(1).toLowerCase();

        ItemStack item = ItemFactory.create(name, Material.WOOL, data);

        List<String> lore = new ArrayList<>(Arrays.asList(
                KEY + "id: " + VALUE + transaction.getID(),
                KEY + "recipient: " + VALUE + transaction.getRecipientAccountNumber()
        ));
        if(transaction instanceof Transfer) lore.add(KEY + "sender: " + VALUE + ((Transfer) transaction).getSenderAccountNumber());
        if(!(transaction instanceof Interest)) lore.add(KEY + "amount: " + VALUE + Utils.formatMoney(transaction.getAmount()));
        if(transaction instanceof Interest) lore.add(KEY + "interestRate: " + VALUE + ((Interest) transaction).getInterestRate()*100 + "%");
        lore.add(KEY + "date: " + VALUE + transaction.getDate().toString());

        return ItemFactory.setLore(item, lore);
    }

}
