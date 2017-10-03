package me.dirantos.moneymaker.components.command;

import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.components.chat.ChatMessenger;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    private final CommandInfo info;
    private MoneyMakerPlugin plugin;
    private ChatMessenger messenger;


    public SubCommand() {
        if(!getClass().isAnnotationPresent(CommandInfo.class)) throw new IllegalStateException("Every command must be annotated with CommandInfo");
        this.info = getClass().getAnnotation(CommandInfo.class);
    }

    protected abstract void handle(CommandSender sender, String[] args);

    public final void sendUsage(CommandSender sender, String name) {
        messenger.send(sender, "[[/" + name + " " + getInfo().usage() + " ]]");
    }

    public final void setPlugin(MoneyMakerPlugin plugin) {
        this.plugin = plugin;
        this.messenger = plugin.getChatMessenger();
    }

    public final CommandInfo getInfo() {
        return info;
    }

    public final MoneyMakerPlugin getPlugin() {
        return plugin;
    }

    public final ChatMessenger getMessenger() {
        return messenger;
    }

}
