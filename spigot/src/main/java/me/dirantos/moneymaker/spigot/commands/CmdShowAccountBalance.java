package me.dirantos.moneymaker.spigot.commands;

import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.api.service.MoneyMakerAPIService;
import me.dirantos.moneymaker.spigot.chat.ChatLevel;
import me.dirantos.moneymaker.spigot.command.CommandInfo;
import me.dirantos.moneymaker.spigot.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "balance", permission = "moneymaker.cmd.account.showbalance", usage = "balance [account-number]", description = {"displays the balance of the account", "__player only!__"})
public class CmdShowAccountBalance extends SubCommand{

    @Override
    protected void handle(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            getMessanger().send(sender, "This command is only for players!", ChatLevel.ERROR);
            return;
        }

        if(args.length == 0) {
            getMessanger().send(sender, "You have to give the account-number from your account!", ChatLevel.ERROR);
            return;
        }

        int accountNumber;
        try {
            accountNumber = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            getMessanger().send(sender, "Wrong arguments!", ChatLevel.ERROR);
            return;
        }

        MoneyMakerAPIService service = MoneyMakerAPI.getService();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Account account = service.getCache().getAccountCache().get(accountNumber);
            if(account == null) account = service.getAccountFetcher().fetchData(accountNumber);

            if(account == null) {
                getMessanger().send(sender, "The account could not be found!", ChatLevel.ERROR);
                return;
            }

            if(!account.getOwner().equals(((Player) sender).getUniqueId())) {
                getMessanger().send(sender, "The account does not belong to you!", ChatLevel.ERROR);
                return;
            }


            double balance = account.getBalance();
            getMessanger().send(sender, "The account __" + accountNumber + "__ has [[" + balance + "$]]!", ChatLevel.SUCCESS);

        });
    }

}
