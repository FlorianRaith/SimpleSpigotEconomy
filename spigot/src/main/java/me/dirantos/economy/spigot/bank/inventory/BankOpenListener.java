package me.dirantos.economy.spigot.bank.inventory;

import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.spigot.inventory.InventoryManager;
import me.dirantos.economy.spigot.EconomyPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class BankOpenListener implements Listener {

    private final EconomyPlugin plugin;
    private final EconomyService economyService;
    private final InventoryManager inventoryManager;

    public BankOpenListener(EconomyPlugin plugin, EconomyService economyService) {
        this.plugin = plugin;
        this.inventoryManager = new InventoryManager(plugin);
        this.economyService = economyService;

    }

    @EventHandler
    public void onOpen(AsyncBankInventoryOpenEvent event) {
        BankInventory inventory = new BankInventory(event.getAccounts(), plugin, economyService, event.getPlayer());
        inventoryManager.registerInventory(inventory);
        inventoryManager.openInventory(event.getPlayer(), inventory);
    }

}
