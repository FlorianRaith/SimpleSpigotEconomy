package me.dirantos.moneymaker.spigot.commands;

import me.dirantos.moneymaker.api.managers.AccountManager;
import me.dirantos.moneymaker.api.managers.TransactionManager;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.models.Transaction;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.components.chat.ChatLevel;
import me.dirantos.moneymaker.components.command.CommandInfo;
import me.dirantos.moneymaker.components.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandInfo(name = "withdrawal", permission = "moneymaker.cmd.account.withdrawal", usage = "withdrawal [accountNumber] [amount]", description = "withdrawal from your account", playerOnly = true)
public class CmdWithdrawalAccount extends SubCommand {

    @Override
    protected void handle(CommandSender sender, String[] args) {

        if(args.length < 2) {
            getMessanger().send(sender, "You have to give the accountNumber and the amount!", ChatLevel.ERROR);
            return;
        }

        int accountNumber;
        try {
            accountNumber = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            getMessanger().send(sender, "Wrong arguments!", ChatLevel.ERROR);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch(NumberFormatException e) {
            getMessanger().send(sender, "Wrong arguments!", ChatLevel.ERROR);
            return;
        }

        AccountManager accountManager = MoneyMakerAPI.getService().getAccountManager();
        TransactionManager transactionManager = MoneyMakerAPI.getService().getTransactionManager();

        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {

            Optional<Account> account = accountManager.loadAccount(accountNumber);

            if(!account.isPresent()) {
                getMessanger().send(sender, "The account could not be found!", ChatLevel.ERROR);
                return;
            }

            if(!account.get().getOwner().equals(((Player) sender).getUniqueId())) {
                getMessanger().send(sender, "The account does not belong to you!", ChatLevel.ERROR);
                return;
            }

            Transaction transaction = transactionManager.makeWithdrawal(account.get(), amount);
            getMessanger().send(sender, "Successfully withdrawal [[" + transaction.getAmount() + "$]] from __" + transaction.getRecipientAccountNumber() + "__", ChatLevel.SUCCESS);

        });
    }

}
