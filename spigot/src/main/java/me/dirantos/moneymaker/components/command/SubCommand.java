package me.dirantos.moneymaker.components.command;

import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.components.chat.ChatMessanger;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    private final CommandInfo info;
    private MoneyMakerPlugin plugin;
    private ChatMessanger messanger;


    public SubCommand() {
        if(!getClass().isAnnotationPresent(CommandInfo.class)) throw new IllegalStateException("Every command must be annotated with CommandInfo");
        this.info = getClass().getAnnotation(CommandInfo.class);
    }

    protected abstract void handle(CommandSender sender, String[] args);

    public final void setPlugin(MoneyMakerPlugin plugin) {
        this.plugin = plugin;
        this.messanger = plugin.getChatMessanger();
    }

    public CommandInfo getInfo() {
        return info;
    }

    public MoneyMakerPlugin getPlugin() {
        return plugin;
    }

    public ChatMessanger getMessanger() {
        return messanger;
    }

}
