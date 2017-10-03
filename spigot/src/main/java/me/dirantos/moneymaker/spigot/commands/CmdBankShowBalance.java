package me.dirantos.moneymaker.spigot.commands;

import me.dirantos.moneymaker.api.managers.BankManager;
import me.dirantos.moneymaker.api.models.Bank;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.components.chat.ChatLevel;
import me.dirantos.moneymaker.components.command.CommandInfo;
import me.dirantos.moneymaker.components.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "balance", permission = "moneymaker.cmd.bank.showbalance", usage = "balance", description = "displays the current balance", playerOnly = true)
public class CmdBankShowBalance extends SubCommand {

    @Override
    protected void handle(CommandSender sender, String[] args) {

        Player player = ((Player) sender);

        if(player == null) {
            getMessenger().send(sender, "The player [[" + args[0] + "]] is not online!", ChatLevel.ERROR);
            return;
        }

        BankManager bankManager = MoneyMakerAPI.getService().getBankManager();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Bank bank = bankManager.loadBank(player.getUniqueId());

            double balance = bank.getMoney();
            getMessenger().send(sender, "Your current balance is [[" + balance + "$]]!", ChatLevel.SUCCESS);
        });

    }

}
