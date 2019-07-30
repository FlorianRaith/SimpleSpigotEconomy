package me.dirantos.economy.spigot.command;


import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.spigot.chat.ChatLevel;
import me.dirantos.economy.spigot.EconomyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "create", permission = "economy.cmd.account.create", usage = "create", description = "creates a new account", playerOnly = true)
public class CreateAccountCommand extends SubCommand {

    public CreateAccountCommand(EconomyPlugin plugin, EconomyService economyService) {
        super(plugin, economyService);
    }

    @Override
    protected void handle(CommandSender sender, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Account account = getEconomyService().createNewAccount(((Player) sender));
            getMessenger().send(sender, "Your account was successfully created.", ChatLevel.SUCCESS);
            getMessenger().send(sender, "The accountNumber is __" + account.getID() + "__.", ChatLevel.SUCCESS);
        });
    }

}
