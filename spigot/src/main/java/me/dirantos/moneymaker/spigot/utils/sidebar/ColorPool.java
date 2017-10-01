package me.dirantos.moneymaker.spigot.utils.sidebar;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class ColorPool {

    private final List<ChatColor> colors = new ArrayList<>(Arrays.asList(ChatColor.values()));
    private final List<ChatColor> colorsUsed = new ArrayList<>();

    public ChatColor getFreeColor() {
        List<ChatColor> freeColors = new ArrayList<>();
        for (ChatColor color : colors) if (!colorsUsed.contains(color)) freeColors.add(color);
        return freeColors.get(0);
    }

    public void clear() {
        colorsUsed.clear();
    }

    public void add(ChatColor chatColor) {
        colorsUsed.add(chatColor);
    }

}
