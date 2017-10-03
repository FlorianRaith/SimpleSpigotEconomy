package me.dirantos.moneymaker.spigot.commands;

import me.dirantos.moneymaker.api.managers.BankManager;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.components.command.CommandInfo;
import me.dirantos.moneymaker.components.command.SubCommand;
import me.dirantos.moneymaker.spigot.bank.AsyncBankInventoryOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

@CommandInfo(name = "open", permission = "moneymaker.cmd.bank.open", usage = "open", description = "opens the bank", playerOnly = true)
public class CmdBankOpen extends SubCommand {

    @Override
    protected void handle(CommandSender sender, String[] args) {
        BankManager bankManager = MoneyMakerAPI.getService().getBankManager();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Set<Account> accounts = bankManager.loadAccounts(bankManager.loadBank((Player) sender));
            Bukkit.getPluginManager().callEvent(new AsyncBankInventoryOpenEvent(accounts, (Player) sender));
        });
    }

}
