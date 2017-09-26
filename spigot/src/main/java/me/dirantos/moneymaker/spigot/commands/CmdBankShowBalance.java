package me.dirantos.moneymaker.spigot.commands;

import me.dirantos.moneymaker.api.managers.BankManager;
import me.dirantos.moneymaker.api.models.Bank;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.spigot.chat.ChatLevel;
import me.dirantos.moneymaker.spigot.command.CommandInfo;
import me.dirantos.moneymaker.spigot.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "balance", permission = "moneymaker.cmd.bank.showbalance", usage = "balance", description = "displays the current balance", playerOnly = true)
public class CmdBankShowBalance extends SubCommand {

    @Override
    protected void handle(CommandSender sender, String[] args) {

        BankManager bankManager = MoneyMakerAPI.getService().getBankManager();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Bank bank = bankManager.loadBank(((Player) sender).getUniqueId());

            double balance = bank.getMoney();
            getMessanger().send(sender, "Your current balance is [[" + balance + "$]]!", ChatLevel.SUCCESS);
        });

    }

}
