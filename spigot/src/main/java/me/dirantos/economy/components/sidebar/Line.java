package me.dirantos.economy.components.sidebar;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

final class Line {
    private ChatColor color;
    private int index;
    private Team team;
    private String text;
    private String entry;
    private String parameteredText;

    Line(int index, Team team, String entry, ChatColor color) {
        this.index = index;
        this.team = team;
        this.entry = entry;
        this.color = color;
    }

    int getIndex() {
        return index;
    }

    Team getTeam() {
        return team;
    }

    String getText() {
        return text;
    }

    void setText(String text) {
        this.text = text;
    }

    String getEntry() {
        return entry;
    }

    ChatColor getColor() {
        return color;
    }

    String getParameteredText() {
        return parameteredText;
    }

    void setParameteredText(String parameteredText) {
        this.parameteredText = parameteredText;
    }

}