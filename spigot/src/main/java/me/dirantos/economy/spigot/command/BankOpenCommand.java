package me.dirantos.economy.spigot.command;

import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.spigot.EconomyPlugin;
import me.dirantos.economy.spigot.bank.inventory.AsyncBankInventoryOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

@CommandInfo(name = "open", permission = "economy.cmd.bank.open", usage = "open", description = "opens the bank", playerOnly = true)
public class BankOpenCommand extends SubCommand {

    public BankOpenCommand(EconomyPlugin plugin, EconomyService economyService) {
        super(plugin, economyService);
    }

    @Override
    protected void handle(CommandSender sender, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Set<Account> accounts = getEconomyService().loadPlayerAccounts((Player) sender);
            Bukkit.getPluginManager().callEvent(new AsyncBankInventoryOpenEvent(accounts, (Player) sender));
        });
    }

}
