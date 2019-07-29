package me.dirantos.economy.spigot.commands;

import me.dirantos.economy.api.managers.BankManager;
import me.dirantos.economy.api.models.Bank;
import me.dirantos.economy.api.service.EconomyService;
import me.dirantos.economy.components.chat.ChatLevel;
import me.dirantos.economy.components.command.CommandInfo;
import me.dirantos.economy.components.command.SubCommand;
import me.dirantos.economy.spigot.EconomyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "balance", permission = "economy.cmd.bank.showbalance", usage = "balance", description = "displays the current balance", playerOnly = true)
public class BankShowBalanceCommand extends SubCommand {

    public BankShowBalanceCommand(EconomyPlugin plugin, EconomyService economyService) {
        super(plugin, economyService);
    }

    @Override
    protected void handle(CommandSender sender, String[] args) {

        Player player = ((Player) sender);

        if(player == null) {
            getMessenger().send(sender, "The player [[" + args[0] + "]] is not online!", ChatLevel.ERROR);
            return;
        }

        BankManager bankManager = getEconomyService().getBankManager();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Bank bank = bankManager.loadBank(player.getUniqueId());

            double balance = bank.getMoney();
            getMessenger().send(sender, "Your current balance is [[" + balance + "$]]!", ChatLevel.SUCCESS);
        });

    }

}
