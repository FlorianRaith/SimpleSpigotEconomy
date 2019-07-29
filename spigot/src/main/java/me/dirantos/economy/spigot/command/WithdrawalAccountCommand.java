package me.dirantos.economy.spigot.command;

import me.dirantos.economy.api.account.AccountManager;
import me.dirantos.economy.api.transaction.TransactionManager;
import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.transaction.Transaction;
import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.spigot.chat.ChatLevel;
import me.dirantos.economy.spigot.command.CommandInfo;
import me.dirantos.economy.spigot.command.SubCommand;
import me.dirantos.economy.spigot.EconomyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandInfo(name = "withdrawal", permission = "economy.cmd.account.withdrawal", usage = "withdrawal [accountNumber] [amount]", description = "withdrawal from your account", playerOnly = true)
public class WithdrawalAccountCommand extends SubCommand {

    public WithdrawalAccountCommand(EconomyPlugin plugin, EconomyService economyService) {
        super(plugin, economyService);
    }

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

        AccountManager accountManager = getEconomyService().getAccountManager();
        TransactionManager transactionManager = getEconomyService().getTransactionManager();

        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {

            Optional<Account> account = accountManager.loadAccount(accountNumber);

            if(!account.isPresent()) {
                getMessenger().send(sender, "The account could not be found!", ChatLevel.ERROR);
                return;
            }

            if(!account.get().getOwner().equals(((Player) sender).getUniqueId())) {
                getMessenger().send(sender, "The account does not belong to you!", ChatLevel.ERROR);
                return;
            }

            if(account.get().getBalance() - amount < 0) {
                getMessenger().send(sender, "Your account has not enough money!", ChatLevel.ERROR);
                return;
            }

            Transaction transaction = transactionManager.makeWithdrawal(account.get(), amount);
            getMessenger().send(sender, "Successfully withdrawal [[" + transaction.getAmount() + "$]] from __" + transaction.getRecipientAccountNumber() + "__", ChatLevel.SUCCESS);

        });
    }

}
