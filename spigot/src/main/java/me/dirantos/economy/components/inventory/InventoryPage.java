package me.dirantos.economy.components.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class InventoryPage {

    private final int height;
    private final String displayName;
    private final InventoryBackground background;
    private final Map<Integer, InventoryItem> items = new HashMap<>();

    public InventoryPage(int height, String displayName, InventoryBackground background) {
        this.height = height;
        this.displayName = displayName;
        this.background = background;
    }

    public abstract void build();

    protected final void registerItem(int slot, InventoryItem inventoryItem) {
            items.put(slot, inventoryItem);
    }

    protected final void registerItem(int slot, ItemStack itemStack) {
        items.put(slot, new EmptyItem(itemStack));
    }

    public final int getHeight() {
        return height;
    }

    public final String getDisplayName() {
        return displayName;
    }

    public final InventoryBackground getBackground() {
        return background;
    }

    public final Map<Integer, InventoryItem> getItems() {
        return items;
    }

    private static final class EmptyItem extends InventoryItem {

        private final ItemStack itemStack;

        public EmptyItem(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public ItemStack createItem() {
            return itemStack;
        }

        @Override
        public void onClick(InventoryClickEvent event) {
            event.setCancelled(true);
        }

    }

}
