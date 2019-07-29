package me.dirantos.economy.spigot;

import me.dirantos.economy.api.managers.BankManager;
import me.dirantos.economy.api.managers.TransactionManager;
import me.dirantos.economy.api.models.Account;
import me.dirantos.economy.components.chat.ChatLevel;
import me.dirantos.economy.components.chat.ChatMessenger;
import me.dirantos.economy.spigot.configs.InterestConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Set;

public class InterestReceiver {

    private final EconomyPlugin plugin;
    private final ChatMessenger messenger;
    private final InterestConfig interestConfig;
    private final BankManager bankManager;
    private final TransactionManager transactionManager;
    private final int delay;
    private int schedulerID;

    public InterestReceiver(EconomyPlugin plugin, int delay, InterestConfig interestConfig, BankManager bankManager, TransactionManager transactionManager) {
        this.plugin = plugin;
        this.delay = delay;
        this.interestConfig = interestConfig;
        this.messenger = plugin.getChatMessenger();
        this.bankManager = bankManager;
        this.transactionManager = transactionManager;
    }

    public void start() {
        schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () ->  {

            Collection<? extends Player> players = Bukkit.getOnlinePlayers();

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                for (Player player : players) {
                    Set<Account> accounts = bankManager.loadAccounts(bankManager.loadBank(player));
                    for (Account account : accounts) {
                        transactionManager.makeInterest(account, interestConfig.getInterestRate());
                    }

                    messenger.send(player, "You have received [[" + interestConfig.getInterestRate() * 100 + "%]] interest", ChatLevel.SUCCESS);
                }
            });
        }, delay, delay);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(schedulerID);
    }

}
