package me.dirantos.economy.spigot;

import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.spigot.chat.ChatLevel;
import me.dirantos.economy.spigot.chat.ChatMessenger;
import me.dirantos.economy.spigot.config.InterestConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Set;

public class InterestReceiver {

    private final EconomyPlugin plugin;
    private final ChatMessenger messenger;
    private final InterestConfig interestConfig;
    private final EconomyService economyService;
    private final int delay;
    private int schedulerID;

    public InterestReceiver(EconomyPlugin plugin, int delay, InterestConfig interestConfig, EconomyService economyService) {
        this.plugin = plugin;
        this.delay = delay;
        this.interestConfig = interestConfig;
        this.messenger = plugin.getChatMessenger();
        this.economyService = economyService;
    }

    public void start() {
        schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () ->  {

            Collection<? extends Player> players = Bukkit.getOnlinePlayers();

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                for (Player player : players) {
                    Set<Account> accounts = economyService.loadPlayerAccounts(player);
                    for (Account account : accounts) {
                        economyService.interest(account, interestConfig.getInterestRate());
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
