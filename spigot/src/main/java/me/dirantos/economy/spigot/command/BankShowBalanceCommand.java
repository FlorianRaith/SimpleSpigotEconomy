package me.dirantos.economy.spigot.command;

import me.dirantos.economy.api.bank.Bank;
import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.spigot.chat.ChatLevel;
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

        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Bank bank = getEconomyService().loadBank(player);
            double walletBalance = bank.getWalletBalance();
            double bankBalance = bank.getBankBalance();
            getMessenger().send(sender, "Your current wallet balance is [[" + walletBalance + "$]]!", ChatLevel.SUCCESS);
            getMessenger().send(sender, "Your current bank balance is [[" + bankBalance + "$]]!", ChatLevel.SUCCESS);
        });

    }

}
