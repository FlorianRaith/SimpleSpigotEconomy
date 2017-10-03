package me.dirantos.moneymaker.spigot;

import me.dirantos.moneymaker.api.managers.BankManager;
import me.dirantos.moneymaker.api.managers.TransactionManager;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.components.chat.ChatLevel;
import me.dirantos.moneymaker.components.chat.ChatMessenger;
import me.dirantos.moneymaker.spigot.configs.InterestConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Set;

public class InterestReceiver {

    private final MoneyMakerPlugin plugin;
    private final ChatMessenger messenger;
    private final InterestConfig interestConfig;
    private final int delay;
    private int schedulerID;

    public InterestReceiver(MoneyMakerPlugin plugin, int delay, InterestConfig interestConfig) {
        this.plugin = plugin;
        this.delay = delay;
        this.interestConfig = interestConfig;
        this.messenger = plugin.getChatMessenger();
    }

    public void start() {
        schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () ->  {

            Collection<? extends Player> players = Bukkit.getOnlinePlayers();

            TransactionManager transactionManager = MoneyMakerAPI.getService().getTransactionManager();
            BankManager bankManager = MoneyMakerAPI.getService().getBankManager();

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
