package me.dirantos.economy.components.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ChatMessenger {

    private static final Pattern ITALICS_PATTERN = Pattern.compile("__(\\S[\\s\\S]*?)__");
    private static final Pattern BOLD_PATTERN = Pattern.compile("\\*\\*(\\S[\\s\\S]*?)\\*\\*");
    private static final Pattern HIGHLIGHT_PATTERN = Pattern.compile("\\[\\[(\\S[\\s\\S]*?)\\]\\]");

    private final String prefix;

    public ChatMessenger(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Sends a automatic formatted and prefixed message to the reciever
     *
     * **message** turns bold
     * __message__ turns to italics
     * [[message]] highlights the text
     *
     * the chatlevel indicates the colors to use for the normal message and the highlighted one
     *
     * @param reciever the reciever
     * @param message the message
     * @param chatLevel the chatlevel
     */
    public void send(CommandSender reciever, String message, ChatLevel chatLevel) {
        reciever.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + chatLevel.getColor() + format(message, chatLevel));
    }

    public void send(CommandSender reciever, String message) {
        send(reciever, message, ChatLevel.INFO);
    }

    public void sendAll(String message, ChatLevel chatLevel) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            send(player, message, chatLevel);
        }
    }

    public void sendAll(String message) {
        sendAll(message, ChatLevel.INFO);
    }

    private String format(String input, ChatLevel chatLevel) {
        Matcher italicsMatcher = ITALICS_PATTERN.matcher(input);
        while(italicsMatcher.find()) {
            String found = italicsMatcher.group();
            input = input.replace(found, parse(found, ChatColor.ITALIC + "", ChatColor.RESET + "" + chatLevel.getColor()));
        }

        Matcher boldMatcher = BOLD_PATTERN.matcher(input);
        while(boldMatcher.find()) {
            String found = boldMatcher.group();
            input = input.replace(found, parse(found, ChatColor.BOLD + "", ChatColor.RESET + "" + chatLevel.getColor()));
        }

        Matcher highlightMatcher = HIGHLIGHT_PATTERN.matcher(input);
        while(highlightMatcher.find()) {
            String found = highlightMatcher.group();
            input = input.replace(found, parse(found, chatLevel.getHighlightColor() + "", ChatColor.RESET + "" + chatLevel.getColor()));
        }

        return input;
    }

    private String parse(String text, String begin, String end) {
        return begin + text.substring(2, text.length() - 2) + end;
    }


}
