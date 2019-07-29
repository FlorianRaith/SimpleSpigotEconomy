package me.dirantos.economy.spigot.bank;

import me.dirantos.economy.api.managers.AccountManager;
import me.dirantos.economy.components.inventory.InventoryManager;
import me.dirantos.economy.spigot.EconomyPlugin;
import me.dirantos.economy.spigot.bank.inventories.BankInventory;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class BankOpenListener implements Listener {

    private final EconomyPlugin plugin;
    private final AccountManager accountManager;
    private final InventoryManager inventoryManager;

    public BankOpenListener(EconomyPlugin plugin, AccountManager accountManager) {
        this.plugin = plugin;
        this.inventoryManager = new InventoryManager(plugin);
        this.accountManager = accountManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    public void onOpen(AsyncBankInventoryOpenEvent event) {
        BankInventory inventory = new BankInventory(event.getAccounts(), plugin, accountManager, event.getPlayer());
        inventoryManager.registerInventory(inventory);
        inventoryManager.openInventory(event.getPlayer(), inventory);
    }

}
