package me.dirantos.economy.spigot.command;

import me.dirantos.economy.api.account.AccountManager;
import me.dirantos.economy.api.transaction.TransactionManager;
import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.transaction.Transfer;
import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.spigot.chat.ChatLevel;
import me.dirantos.economy.spigot.command.CommandInfo;
import me.dirantos.economy.spigot.command.SubCommand;
import me.dirantos.economy.spigot.EconomyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Optional;

@CommandInfo(name = "transfer", permission = "economy.cmd.account.transfer", usage = "transfer [own-accountNumber] [accoutNumber] [amount]", description = "transfers an amount of money for your managers to another managers", playerOnly = true)
public class TransferAccountCommand extends SubCommand {

    public TransferAccountCommand(EconomyPlugin plugin, EconomyService economyService) {
        super(plugin, economyService);
    }

    @Override
    protected void handle(CommandSender sender, String[] args) {

        if(args.length < 3) {
            sendUsage(sender, "account");
            return;
        }

        int ownAccountNumber;
        try {
            ownAccountNumber = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            getMessenger().send(sender, "Wrong arguments!", ChatLevel.ERROR);
            return;
        }

        int otherAccountNumber;
        try {
            otherAccountNumber = Integer.parseInt(args[1]);
        } catch(NumberFormatException e) {
            getMessenger().send(sender, "Wrong arguments!", ChatLevel.ERROR);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[2]);
        } catch(NumberFormatException e) {
            getMessenger().send(sender, "Wrong arguments!", ChatLevel.ERROR);
            return;
        }

        TransactionManager transactionManager = getEconomyService().getTransactionManager();
        AccountManager accountManager = getEconomyService().getAccountManager();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {

            Optional<Account> ownAccount = accountManager.loadAccount(ownAccountNumber);
            Optional<Account> otherAccount = accountManager.loadAccount(otherAccountNumber);

            if(!ownAccount.isPresent() || !otherAccount.isPresent()) {
                getMessenger().send(sender, "accountNumbers are invalid!", ChatLevel.ERROR);
                return;
            }

            if(ownAccount.get().getBalance() - amount < 0) {
                getMessenger().send(sender, "Your account has not enough money", ChatLevel.ERROR);
                return;
            }

            Transfer transfer = transactionManager.makeTransfer(otherAccount.get(), ownAccount.get(), amount);
            getMessenger().send(sender, "Successfully transfered [[" + transfer.getAmount() + "$]] from __" + transfer.getSenderAccountNumber() + "__ to __" + transfer.getRecipientAccountNumber() + "__", ChatLevel.SUCCESS);
        });


    }

}
