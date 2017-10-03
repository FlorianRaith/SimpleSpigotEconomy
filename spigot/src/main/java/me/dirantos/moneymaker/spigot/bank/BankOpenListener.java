package me.dirantos.moneymaker.spigot.bank;

import me.dirantos.moneymaker.components.inventory.InventoryManager;
import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.spigot.bank.inventories.BankInventory;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class BankOpenListener implements Listener {

    private final MoneyMakerPlugin plugin;
    private final InventoryManager inventoryManager;

    public BankOpenListener(MoneyMakerPlugin plugin) {
        this.plugin = plugin;
        this.inventoryManager = new InventoryManager(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    public void onOpen(AsyncBankInventoryOpenEvent event) {
        BankInventory inventory = new BankInventory(event.getAccounts(), plugin, event.getPlayer());
        inventoryManager.registerInventory(inventory);
        inventoryManager.openInventory(event.getPlayer(), inventory);
    }

}
