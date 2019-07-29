package me.dirantos.economy.spigot.commands;

import me.dirantos.economy.api.managers.BankManager;
import me.dirantos.economy.api.models.Bank;
import me.dirantos.economy.api.service.EconomyService;
import me.dirantos.economy.components.chat.ChatLevel;
import me.dirantos.economy.components.command.CommandInfo;
import me.dirantos.economy.components.command.SubCommand;
import me.dirantos.economy.spigot.EconomyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.UUID;

@CommandInfo(name = "setbalance", permission = "economy.cmd.bank.setbalance", usage = "setbalance [player] [amount]", description = "sets the balance of the bank")
public class BankSetBalanceCommand extends SubCommand {

    public BankSetBalanceCommand(EconomyPlugin plugin, EconomyService economyService) {
        super(plugin, economyService);
    }

    @Override
    protected void handle(CommandSender sender, String[] args) {

        if(args.length < 2) {
            sendUsage(sender, "bank");
            return;
        }

        UUID uuid;
        try {
            OfflinePlayer player = Bukkit.getPlayer(args[0]);
            uuid = player.getUniqueId();
        } catch(Exception e) {
            getMessenger().send(sender, "Wrong arguments!", ChatLevel.ERROR);
            return;
        }

        if(uuid == null) {
            getMessenger().send(sender, "This player does not exists!", ChatLevel.ERROR);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch(NumberFormatException e) {
            getMessenger().send(sender, "Wrong arguments!", ChatLevel.ERROR);
            return;
        }

        BankManager bankManager = getEconomyService().getBankManager();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {

            Bank bank = bankManager.loadBank(uuid);
            bankManager.setBalance(bank, amount);
            getMessenger().send(sender, "The new balance is [[" + amount + "$]]!", ChatLevel.SUCCESS);

        });

    }

}
