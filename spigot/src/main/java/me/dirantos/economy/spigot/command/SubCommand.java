package me.dirantos.economy.spigot.command;

import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.spigot.EconomyPlugin;
import me.dirantos.economy.spigot.chat.ChatMessenger;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    private final CommandInfo info;
    private final EconomyPlugin plugin;
    private final EconomyService economyService;
    private final ChatMessenger messenger;

    public SubCommand(EconomyPlugin plugin, EconomyService economyService) {
        this.plugin = plugin;
        this.economyService = economyService;
        this.messenger = plugin.getChatMessenger();
        if(!getClass().isAnnotationPresent(CommandInfo.class)) throw new IllegalStateException("Every command must be annotated with CommandInfo");
        this.info = getClass().getAnnotation(CommandInfo.class);
    }

    protected abstract void handle(CommandSender sender, String[] args);

    public final void sendUsage(CommandSender sender, String name) {
        messenger.send(sender, "[[/" + name + " " + getInfo().usage() + " ]]");
    }

    public final CommandInfo getInfo() {
        return info;
    }

    public final EconomyPlugin getPlugin() {
        return plugin;
    }

    public final EconomyService getEconomyService() {
        return economyService;
    }

    public final ChatMessenger getMessenger() {
        return messenger;
    }

}
