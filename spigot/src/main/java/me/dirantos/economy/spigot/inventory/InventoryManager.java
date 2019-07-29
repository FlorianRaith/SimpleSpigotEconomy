package me.dirantos.economy.spigot.inventory;

import me.dirantos.economy.spigot.EconomyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public final class InventoryManager {

    private InventoryListener listener = new InventoryListener();

    public InventoryManager(EconomyPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public void registerInventory(InventoryPage inventoryPage) {
        inventoryPage.build();
        for (InventoryItem inventoryItem : inventoryPage.getItems().values()) {
            listener.register(inventoryItem);
        }
    }

    public void openInventory(Player player, InventoryPage inventoryPage) {
        Inventory inventory = Bukkit.createInventory(null, 9*inventoryPage.getHeight(), inventoryPage.getDisplayName());
        for (int slot : inventoryPage.getItems().keySet()) {
            inventory.setItem(slot, inventoryPage.getItems().get(slot).createItem());
        }
        player.openInventory(inventory);
    }

}
