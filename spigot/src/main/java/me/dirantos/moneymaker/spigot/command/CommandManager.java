package me.dirantos.moneymaker.spigot.command;

import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import org.bukkit.command.CommandExecutor;

public final class CommandManager {

    private final MoneyMakerPlugin plugin;

    public CommandManager(MoneyMakerPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerCommand(AbstractCommand command) {
        command.setPlugin(plugin);
        plugin.getCommand(command.getName()).setExecutor((CommandExecutor) (sender, cmd, label, args) -> {
            if(cmd.getName().equalsIgnoreCase(command.getName())) {
                command.handle(sender, args);
                return true;
            }
            return false;
        });
    }

}
