package me.dirantos.moneymaker.spigot.commands;

import me.dirantos.moneymaker.api.managers.BankManager;
import me.dirantos.moneymaker.api.models.Bank;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.spigot.chat.ChatLevel;
import me.dirantos.moneymaker.spigot.command.CommandInfo;
import me.dirantos.moneymaker.spigot.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.UUID;

@CommandInfo(name = "setbalance", permission = "moneymaker.cmd.bank.setbalance", usage = "setbalance [player] [amount]", description = "sets the balance of the bank")
public class CmdBankSetBalance extends SubCommand {

    @Override
    protected void handle(CommandSender sender, String[] args) {

        if(args.length < 2) {
            getMessanger().send(sender, "You have to give the player owning the bank and the amount!", ChatLevel.ERROR);
            return;
        }

        UUID uuid;
        try {
            OfflinePlayer player = Bukkit.getPlayer(args[0]);
            uuid = player.getUniqueId();
        } catch(Exception e) {
            getMessanger().send(sender, "Wrong arguments!", ChatLevel.ERROR);
            return;
        }

        if(uuid == null) {
            getMessanger().send(sender, "This player does not exists!", ChatLevel.ERROR);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch(NumberFormatException e) {
            getMessanger().send(sender, "Wrong arguments!", ChatLevel.ERROR);
            return;
        }

        BankManager bankManager = MoneyMakerAPI.getService().getBankManager();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {

            Bank bank = bankManager.loadBank(uuid);
            bank.setMoney(amount);
            getMessanger().send(sender, "The new balance is [[" + amount + "$]]!", ChatLevel.SUCCESS);

        });

    }

}
