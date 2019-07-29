package me.dirantos.economy.spigot.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public final class ItemFactory {

    private ItemFactory() {}

    public static ItemStack create(String displayName, Material material, int amount, short damage) {
        ItemStack itemStack = new ItemStack(material, amount, damage);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack create(String displayName, Material material, int amount) {
        return create(displayName, material, amount, (short) 0);
    }

    public static ItemStack create(String displayName, Material material, short damage) {
        return create(displayName, material, 1, damage);
    }

    public static ItemStack setLore(ItemStack item, String... lore) {
        return setLore(item, Arrays.asList(lore));
    }

    public static ItemStack setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

}
