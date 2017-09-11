package me.dirantos.moneymaker.spigot.command;

import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.spigot.chat.ChatMessanger;
import org.bukkit.command.CommandSender;

public abstract class AbstractCommand {

    private final String name;
    private MoneyMakerPlugin plugin;
    private ChatMessanger messanger;

    public AbstractCommand(String name) {
        this.name = name;
    }

    protected abstract void handle(CommandSender sender, String[] args);

    public final void setPlugin(MoneyMakerPlugin plugin) {
        this.plugin = plugin;
        this.messanger = plugin.getChatMessanger();
    }

    public final String getName() {
        return name;
    }

    public MoneyMakerPlugin getPlugin() {
        return plugin;
    }

    public ChatMessanger getMessanger() {
        return messanger;
    }

}
