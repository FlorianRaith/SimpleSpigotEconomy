package me.dirantos.economy.spigot.commands;

import me.dirantos.economy.api.managers.AccountManager;
import me.dirantos.economy.api.managers.BankManager;
import me.dirantos.economy.api.managers.TransactionManager;
import me.dirantos.economy.api.models.Account;
import me.dirantos.economy.api.models.Bank;
import me.dirantos.economy.api.models.Transaction;
import me.dirantos.economy.api.service.EconomyService;
import me.dirantos.economy.components.chat.ChatLevel;
import me.dirantos.economy.components.command.CommandInfo;
import me.dirantos.economy.components.command.SubCommand;
import me.dirantos.economy.spigot.EconomyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandInfo(name = "deposit", permission = "economy.cmd.account.deposit", usage = "deposit [accountNumber] [amount]", description = "deposit to your account", playerOnly = true)
public class DepositAccountCommand extends SubCommand {

    public DepositAccountCommand(EconomyPlugin plugin, EconomyService economyService) {
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
        BankManager bankManager = getEconomyService().getBankManager();

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
