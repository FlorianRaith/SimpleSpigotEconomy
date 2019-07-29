package me.dirantos.economy.components.chat;

import org.bukkit.ChatColor;

public enum ChatLevel {

    INFO(ChatColor.GRAY, ChatColor.YELLOW),
    ERROR(ChatColor.RED, ChatColor.DARK_RED),
    SUCCESS(ChatColor.GREEN, ChatColor.DARK_GREEN),
    ALERT(ChatColor.YELLOW, ChatColor.GOLD);

    private final ChatColor color;
    private final ChatColor highlightColor;

    ChatLevel(ChatColor color, ChatColor highlightColor) {
        this.color = color;
        this.highlightColor = highlightColor;
    }

    public ChatColor getColor() {
        return color;
    }

    public ChatColor getHighlightColor() {
        return highlightColor;
    }

}
