package me.dirantos.moneymaker.spigot.commands;

import me.dirantos.moneymaker.api.managers.AccountManager;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.spigot.chat.ChatLevel;
import me.dirantos.moneymaker.spigot.command.CommandInfo;
import me.dirantos.moneymaker.spigot.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandInfo(name = "balance", permission = "moneymaker.cmd.account.showbalance", usage = "balance accountNumber]", description = "displays the balance of the account", playerOnly = true)
public class CmdShowAccountBalance extends SubCommand{

    @Override
    protected void handle(CommandSender sender, String[] args) {
        if(args.length == 0) {
            getMessanger().send(sender, "You have to give the accountNumber from your account!", ChatLevel.ERROR);
            return;
        }

        int accountNumber;
        try {
            accountNumber = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            getMessanger().send(sender, "Wrong arguments!", ChatLevel.ERROR);
            return;
        }

        AccountManager accountManager = MoneyMakerAPI.getService().getAccountManager();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Optional<Account> account = accountManager.loadAccount(accountNumber);

            if(!account.isPresent()) {
                getMessanger().send(sender, "The account could not be found!", ChatLevel.ERROR);
                return;
            }

            if(!account.get().getOwner().equals(((Player) sender).getUniqueId())) {
                getMessanger().send(sender, "The account does not belong to you!", ChatLevel.ERROR);
                return;
            }


            double balance = account.get().getBalance();
            getMessanger().send(sender, "The account __" + accountNumber + "__ has [[" + balance + "$]]!", ChatLevel.SUCCESS);

        });
    }

}
