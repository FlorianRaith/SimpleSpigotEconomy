package me.dirantos.moneymaker.spigot.commands;

import me.dirantos.moneymaker.api.managers.AccountManager;
import me.dirantos.moneymaker.api.managers.BankManager;
import me.dirantos.moneymaker.api.managers.TransactionManager;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.models.Bank;
import me.dirantos.moneymaker.api.models.Transaction;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.components.chat.ChatLevel;
import me.dirantos.moneymaker.components.command.CommandInfo;
import me.dirantos.moneymaker.components.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandInfo(name = "deposit", permission = "moneymaker.cmd.account.deposit", usage = "deposit [accountNumber] [amount]", description = "deposit to your account", playerOnly = true)
public class CmdDepositAccount extends SubCommand {

    @Override
    protected void handle(CommandSender sender, String[] args) {

        if(args.length < 2) {
            sendUsage(sender, "account");
            return;
        }

        int accountNumber;
        try {
            accountNumber = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            getMessenger().send(sender, "Wrong arguments!", ChatLevel.ERROR);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch(NumberFormatException e) {
            getMessenger().send(sender, "Wrong arguments!", ChatLevel.ERROR);
            return;
        }

        AccountManager accountManager = MoneyMakerAPI.getService().getAccountManager();
        TransactionManager transactionManager = MoneyMakerAPI.getService().getTransactionManager();
        BankManager bankManager = MoneyMakerAPI.getService().getBankManager();

        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {

            Bank bank = bankManager.loadBank(((Player) sender));
            if(bank.getMoney() < amount) {
                getMessenger().send(sender, "You have not enough money!", ChatLevel.ERROR);
                return;
            }

            Optional<Account> account = accountManager.loadAccount(accountNumber);


            if(!account.isPresent()) {
                getMessenger().send(sender, "The account could not be found!", ChatLevel.ERROR);
                return;
            }

            if(!account.get().getOwner().equals(((Player) sender).getUniqueId())) {
                getMessenger().send(sender, "The account does not belong to you!", ChatLevel.ERROR);
                return;
            }

            Transaction transaction = transactionManager.makeDeposit(account.get(), amount);
            getMessenger().send(sender, "Successfully deposit [[" + transaction.getAmount() + "$]] to __" + transaction.getRecipientAccountNumber() + "__", ChatLevel.SUCCESS);

        });

    }

}
