package me.dirantos.economy.spigot.bank.inventory;

import me.dirantos.economy.spigot.inventory.InventoryItem;
import me.dirantos.economy.spigot.inventory.InventoryManager;
import me.dirantos.economy.spigot.inventory.ItemFactory;
import me.dirantos.economy.spigot.bank.inventory.BankInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ReturnItem extends InventoryItem {

    private BankInventory bankInventory;
    private InventoryManager inventoryManager;
    private Player player;

    public ReturnItem(BankInventory bankInventory, InventoryManager inventoryManager, Player player) {
        this.bankInventory = bankInventory;
        this.inventoryManager = inventoryManager;
        this.player = player;
    }

    @Override
    public ItemStack createItem() {
        return ItemFactory.create(ChatColor.BLUE + "Go back - " + player.getName(), Material.WOOD_DOOR, 1);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        inventoryManager.openInventory(player, bankInventory);
        event.setCancelled(true);
    }

}
