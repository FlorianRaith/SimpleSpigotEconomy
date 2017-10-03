package me.dirantos.moneymaker.components.command;

import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.components.chat.ChatLevel;
import me.dirantos.moneymaker.components.chat.ChatMessanger;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public final class Command {

    private final String name;
    private final MoneyMakerPlugin plugin;
    private final ChatMessanger messanger;
    private final Set<SubCommand> commands = new HashSet<>();

    public Command(String name, MoneyMakerPlugin plugin) {
        this.name = name;
        this.plugin = plugin;
        this.messanger = plugin.getChatMessanger();
    }

    public void addSubCommand(SubCommand command) {
        command.setPlugin(plugin);
        commands.add(command);
    }

    public void register() {
        plugin.getCommand(name).setExecutor((sender, cmd, label, args) -> {

            // show help message
            if(args.length == 0) {
                messanger.send(sender, "**================**");
                messanger.send(sender, "");
                for (SubCommand command : commands) {
                    messanger.send(sender, "[[/" + name + " " + command.getInfo().usage() + " ]]");
                    for (String desc : command.getInfo().description()) {
                        messanger.send(sender, "  " + desc);
                    }
                }
                messanger.send(sender, "");
                messanger.send(sender, "**================**");
                return true;
            }

            // handle commands
            if(args.length >= 1) {
                for (SubCommand command : commands) {
                    if(args[0].equalsIgnoreCase(command.getInfo().name())) {
                        // check permissions
                        if(!sender.hasPermission(command.getInfo().permission())) {
                            messanger.send(sender, "You don't have permissions to use this command!", ChatLevel.ERROR);
                            return true;
                        }

                        String[] newArgs = new String[args.length - 1];
                        for (int i = 1; i < args.length; i++) {
                            newArgs[i-1] = args[i];
                        }

                        if(command.getInfo().playerOnly() && !(sender instanceof Player)) {
                            messanger.send(sender, "This command is only for players!", ChatLevel.ERROR);
                            return true;
                        }

                        command.handle(sender, newArgs);
                        return true;

                    }
                }
                messanger.send(sender, "The command [[/" + name + " " + args[0] + "]] could not be found!", ChatLevel.ERROR);
                return true;
            }


            return true;
        });
    }

}
