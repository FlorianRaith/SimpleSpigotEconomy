package me.dirantos.economy.components.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class InventoryItem {

    public abstract void onClick(InventoryClickEvent event);

    public abstract ItemStack createItem();

}
