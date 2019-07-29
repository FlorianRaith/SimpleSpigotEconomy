package me.dirantos.economy.spigot.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class InventoryListener implements Listener {

    private Map<String, InventoryItem> items = new HashMap<>();

    public void register(InventoryItem item) {
        items.put(item.createItem().getItemMeta().getDisplayName(), item);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        for (String name : items.keySet()) {
            if(
                    clicked != null &&
                    clicked.hasItemMeta() &&
                    clicked.getItemMeta().hasDisplayName() &&
                    clicked.getItemMeta().getDisplayName().equals(name)
                ) {
                items.get(name).onClick(event);
            }
        }
    }

}
