package me.dirantos.moneymaker.spigot.bank.inventoryitems;

import me.dirantos.moneymaker.api.managers.AccountManager;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.models.Transaction;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.components.inventory.InventoryItem;
import me.dirantos.moneymaker.components.inventory.InventoryManager;
import me.dirantos.moneymaker.components.inventory.ItemFactory;
import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.spigot.bank.inventories.BankInventory;
import me.dirantos.moneymaker.spigot.bank.inventories.TransactionInventory;
import me.dirantos.moneymaker.spigot.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.Set;

public class BankItem extends InventoryItem {

    private static final String KEY = ChatColor.RESET + "" + ChatColor.GRAY;
    private static final String VALUE = ChatColor.YELLOW + "";
    private static InventoryManager manager;

    private final BankInventory bankInventory;
    private final Account account;
    private final Player player;

    public BankItem(MoneyMakerPlugin plugin, Account account, Player player, BankInventory bankInventory) {
        if(manager == null) manager = new InventoryManager(plugin);
        this.account = account;
        this.player = player;
        this.bankInventory = bankInventory;
    }

    @Override
    public ItemStack createItem() {
        ItemStack item = ItemFactory.create(ChatColor.BLUE + "Account - " + account.getAccountNumber(), Material.GOLD_BLOCK, 1);
        return ItemFactory.setLore(item,
                KEY + "balance: " + VALUE + Utils.formatMoney(account.getBalance()),
                KEY + "total transactions: " + VALUE + account.getTransactionIDs().size());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        AccountManager accountManager = MoneyMakerAPI.getService().getAccountManager();
        Set<Transaction> transactions = accountManager.loadAllTransactions(account);
        TransactionInventory inventory = new TransactionInventory(account, transactions, bankInventory, manager, player);
        manager.registerInventory(inventory);
        manager.openInventory(player, inventory);
        event.setCancelled(true);
    }

}
