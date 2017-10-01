package me.dirantos.moneymaker.spigot.listeners;

import me.dirantos.moneymaker.api.managers.BankManager;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.models.Bank;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.api.events.AsyncBankUpdateEvent;
import me.dirantos.moneymaker.spigot.utils.sidebar.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class BankUpdateListener implements Listener {

    private static final String DISPLAY_NAME = ChatColor.DARK_GREEN + "-=[ " + ChatColor.GREEN + "Bank" + ChatColor.DARK_GREEN + " ]=-";

    private final Map<Player, Sidebar> sidebars = new HashMap<>();
    private final MoneyMakerPlugin plugin;

    public BankUpdateListener(MoneyMakerPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void init() {
        BankManager bankManager = MoneyMakerAPI.getService().getBankManager();

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        for (Player player : players) {
            sidebars.put(player, new Sidebar(DISPLAY_NAME));
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for (Player player : players) {
                Bank bank = bankManager.loadBank(player);
                Bukkit.getPluginManager().callEvent(new AsyncBankUpdateEvent(bank, bankManager.loadAccounts(bank)));
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        sidebars.put(event.getPlayer(), new Sidebar(DISPLAY_NAME));

        BankManager bankManager = MoneyMakerAPI.getService().getBankManager();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Bank bank = bankManager.loadBank(event.getPlayer());
            Bukkit.getPluginManager().callEvent(new AsyncBankUpdateEvent(bank, bankManager.loadAccounts(bank)));
        });
    }

    @EventHandler
    public void onUpdate(AsyncBankUpdateEvent event) {
        Player player = Bukkit.getPlayer(event.getBank().getOwner());
        if(player == null) return;

        Sidebar sidebar = sidebars.get(player);
        List<String> lines = new ArrayList<>();

        lines.addAll(Arrays.asList("",
                ChatColor.GREEN +  "Bank balance: " + ChatColor.RESET + event.getBank().getMoney() + ChatColor.GRAY + "$", "",
                ChatColor.GREEN + "Your accounts: "));

        event.getAccounts().stream()
                .sorted(Comparator.comparingInt(Account::getAccountNumber))
                .map(Account::getAccountNumber).map(i -> ChatColor.GRAY + "- " + ChatColor.RESET + i)
                .forEach(lines::add);

        double totalBalance = 0;
        for (Account account : event.getAccounts()) {
            totalBalance += account.getBalance();
        }

        lines.addAll(Arrays.asList("", ChatColor.GREEN + "Account balance: " + ChatColor.RESET + totalBalance + ChatColor.GRAY + "$"));
        sidebar.setLines(lines);
        sidebar.display(player);
    }

}
