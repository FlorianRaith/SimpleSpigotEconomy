package me.dirantos.economy.spigot.commands;

import me.dirantos.economy.api.managers.AccountManager;
import me.dirantos.economy.api.models.Account;
import me.dirantos.economy.api.service.EconomyService;
import me.dirantos.economy.components.chat.ChatLevel;
import me.dirantos.economy.components.command.CommandInfo;
import me.dirantos.economy.components.command.SubCommand;
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

        int accountNumber;
        try {
           accountNumber = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            getMessenger().send(sender, "Wrong arguments!", ChatLevel.ERROR);
            return;
        }

        AccountManager accountManager = getEconomyService().getAccountManager();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Optional<Account> account = accountManager.loadAccount(accountNumber);

            if(!account.isPresent()) {
                getMessenger().send(sender, "The account could not be found!", ChatLevel.ERROR);
                return;
            }

            if(!account.get().getOwner().equals(((Player) sender).getUniqueId())) {
                getMessenger().send(sender, "The managers does not belong to you!", ChatLevel.ERROR);
                return;
            }

            accountManager.deleteAccount(account.get());
            getMessenger().send(sender, "The managers __" + accountNumber + "__ has successfully been deleted!", ChatLevel.SUCCESS);

        });
    }

}
