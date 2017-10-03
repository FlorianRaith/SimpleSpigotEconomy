package me.dirantos.moneymaker.components.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum InventoryBackground {

    NORMAL(Material.THIN_GLASS, (short) 0),
    WHITE(Material.STAINED_GLASS_PANE, (short) 0),
    ORANGE(Material.STAINED_GLASS_PANE, (short) 1),
    MAGENTA(Material.STAINED_GLASS_PANE, (short) 2),
    LIGHT_BLUE(Material.STAINED_GLASS_PANE, (short) 3),
    YELLOW(Material.STAINED_GLASS_PANE, (short) 4),
    LIME(Material.STAINED_GLASS_PANE, (short) 5),
    PINK(Material.STAINED_GLASS_PANE, (short) 6),
    GRAY(Material.STAINED_GLASS_PANE, (short) 7),
    LIGHT_GRAY(Material.STAINED_GLASS_PANE, (short) 8),
    CYAN(Material.STAINED_GLASS_PANE, (short) 9),
    PURPLE(Material.STAINED_GLASS_PANE, (short) 10),
    BLUE(Material.STAINED_GLASS_PANE, (short) 11),
    BROWN(Material.STAINED_GLASS_PANE, (short) 12),
    GREEN(Material.STAINED_GLASS_PANE, (short) 13),
    RED(Material.STAINED_GLASS_PANE, (short) 14),
    BLACK(Material.STAINED_GLASS_PANE, (short) 15),;

    private final ItemStack itemStack;

    InventoryBackground(Material material, short damage) {
        this.itemStack = ItemFactory.create("", material, damage);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

}
