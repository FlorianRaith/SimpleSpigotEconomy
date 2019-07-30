package me.dirantos.economy.spigot.command;

import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.bank.Bank;
import me.dirantos.economy.api.transaction.Transaction;
import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.spigot.chat.ChatLevel;
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

        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {

            Bank bank = getEconomyService().loadBank(((Player) sender));
            if(bank.getWalletBalance() < amount) {
                getMessenger().send(sender, "You have not enough money!", ChatLevel.ERROR);
                return;
            }

            Optional<Account> account = getEconomyService().loadAccount(accountNumber);

            if(!account.isPresent()) {
                getMessenger().send(sender, "The account could not be found!", ChatLevel.ERROR);
                return;
            }

            if(!account.get().getOwner().equals(((Player) sender).getUniqueId())) {
                getMessenger().send(sender, "The account does not belong to you!", ChatLevel.ERROR);
                return;
            }

            Transaction transaction = getEconomyService().deposit(account.get(), amount);
            getMessenger().send(sender, "Successfully deposit [[" + transaction.getAmount() + "$]] to account __#" + transaction.getAccountID() + "__", ChatLevel.SUCCESS);

        });

    }

}
