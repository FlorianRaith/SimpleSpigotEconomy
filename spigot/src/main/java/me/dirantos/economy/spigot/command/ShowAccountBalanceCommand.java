package me.dirantos.economy.spigot.command;

import me.dirantos.economy.api.account.AccountManager;
import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.spigot.chat.ChatLevel;
import me.dirantos.economy.spigot.EconomyPlugin;
import me.dirantos.economy.spigot.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandInfo(name = "balance", permission = "economy.cmd.account.showbalance", usage = "balance [accountNumber]", description = "displays the balance of the account", playerOnly = true)
public class ShowAccountBalanceCommand extends SubCommand{

    public ShowAccountBalanceCommand(EconomyPlugin plugin, EconomyService economyService) {
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
                getMessenger().send(sender, "The account does not belong to you!", ChatLevel.ERROR);
                return;
            }


            double balance = account.get().getBalance();
            getMessenger().send(sender, "The account __" + accountNumber + "__ has [[" + Utils.formatMoney(balance) + "]]!", ChatLevel.SUCCESS);

        });
    }

}
