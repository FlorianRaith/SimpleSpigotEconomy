package me.dirantos.moneymaker.components.command;

import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.components.chat.ChatLevel;
import me.dirantos.moneymaker.components.chat.ChatMessenger;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

public final class Command {

    private final String name;
    private final MoneyMakerPlugin plugin;
    private final ChatMessenger messenger;
    private final Set<SubCommand> commands = new HashSet<>();

    public Command(String name, MoneyMakerPlugin plugin) {
        this.name = name;
        this.plugin = plugin;
        this.messenger = plugin.getChatMessenger();
    }

    public void addSubCommand(SubCommand command) {
        command.setPlugin(plugin);
        commands.add(command);
    }

    public void register() {
        plugin.getCommand(name).setExecutor((sender, cmd, label, args) -> {

            // show help message
            if(args.length == 0) {
                messenger.send(sender, "**================**");
                messenger.send(sender, "");
                for (SubCommand command : commands) {
                    if(!sender.hasPermission(command.getInfo().permission())) continue;
                    command.sendUsage(sender, name);
                    for (String desc : command.getInfo().description()) {
                        messenger.send(sender, "  " + desc);
                    }
                }
                messenger.send(sender, "");
                messenger.send(sender, "**================**");
                return true;
            }

            // handle commands
            if(args.length >= 1) {
                for (SubCommand command : commands) {
                    if(args[0].equalsIgnoreCase(command.getInfo().name())) {
                        // check permissions
                        if(!sender.hasPermission(command.getInfo().permission())) {
                            messenger.send(sender, "You don't have permissions to use this command!", ChatLevel.ERROR);
                            return true;
                        }

                        // create new args-array without the first argument
                        String[] newArgs = new String[args.length - 1];
                        for (int i = 1; i < args.length; i++) {
                            newArgs[i-1] = args[i];
                        }

                        if(command.getInfo().playerOnly() && !(sender instanceof Player)) {
                            messenger.send(sender, "This command is only for players!", ChatLevel.ERROR);
                            return true;
                        }

                        command.handle(sender, newArgs);
                        return true;

                    }
                }
                messenger.send(sender, "The command [[/" + name + " " + args[0] + "]] could not be found!", ChatLevel.ERROR);
                return true;
            }


            return true;
        });

        plugin.getCommand(name).setTabCompleter((sender, cmd, alias, args) -> {
            List<String> names = commands.stream().map(SubCommand::getInfo).map(CommandInfo::name).collect(Collectors.toList());

            List<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], names, completions);

            return args.length == 1 ? completions : new ArrayList<>();
        });
    }

}
