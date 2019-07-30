package me.dirantos.economy.spigot.command;

import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.spigot.chat.ChatLevel;
import me.dirantos.economy.spigot.EconomyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandInfo(name = "delete", permission = "economy.cmd.account.delete", usage = "delete [accountNumber]", description = "deletes a account", playerOnly = true)
public class DeleteAccountCommand extends SubCommand {

    public DeleteAccountCommand(EconomyPlugin plugin, EconomyService economyService) {
        super(plugin, economyService);
    }

    @Override
    protected void handle(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sendUsage(sender, "account");
            return;
        }

        int accountID;
        try {
           accountID = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            getMessenger().send(sender, "Wrong arguments!", ChatLevel.ERROR);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Optional<Account> account = getEconomyService().loadAccount(accountID);

            if(!account.isPresent()) {
                getMessenger().send(sender, "The account could not be found!", ChatLevel.ERROR);
                return;
            }

            if(!account.get().getOwner().equals(((Player) sender).getUniqueId())) {
                getMessenger().send(sender, "The managers does not belong to you!", ChatLevel.ERROR);
                return;
            }

            getEconomyService().deleteAccount(account.get());
            getMessenger().send(sender, "The managers __" + accountID + "__ has successfully been deleted!", ChatLevel.SUCCESS);

        });
    }

}
