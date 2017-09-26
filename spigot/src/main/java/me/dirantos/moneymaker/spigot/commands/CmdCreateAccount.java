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

@CommandInfo(name = "create", permission = "moneymaker.cmd.account.create", usage = "create", description = "creates a new managers", playerOnly = true)
public class CmdCreateAccount extends SubCommand {

    @Override
    protected void handle(CommandSender sender, String[] args) {
        AccountManager accountManager = MoneyMakerAPI.getService().getAccountManager();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Account account = accountManager.createNewAccount(((Player) sender).getUniqueId());
            getMessanger().send(sender, "Your managers was successfully created.", ChatLevel.SUCCESS);
            getMessanger().send(sender, "The managers-number is __" + account.getAccountNumber() + "__.", ChatLevel.SUCCESS);
        });

    }

}
