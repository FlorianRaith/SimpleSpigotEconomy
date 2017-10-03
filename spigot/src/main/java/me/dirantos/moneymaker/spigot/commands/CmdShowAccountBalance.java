package me.dirantos.moneymaker.spigot.commands;

import me.dirantos.moneymaker.api.managers.AccountManager;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.components.chat.ChatLevel;
import me.dirantos.moneymaker.components.command.CommandInfo;
import me.dirantos.moneymaker.components.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandInfo(name = "balance", permission = "moneymaker.cmd.account.showbalance", usage = "balance [accountNumber]", description = "displays the balance of the account", playerOnly = true)
public class CmdShowAccountBalance extends SubCommand{

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

        AccountManager accountManager = MoneyMakerAPI.getService().getAccountManager();
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
            getMessenger().send(sender, "The account __" + accountNumber + "__ has [[" + balance + "$]]!", ChatLevel.SUCCESS);

        });
    }

}
