package me.dirantos.economy.spigot.bank;

import me.dirantos.economy.api.bank.BankManager;
import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.bank.Bank;
import me.dirantos.economy.spigot.EconomyPlugin;
import me.dirantos.economy.api.bank.AsyncBankUpdateEvent;
import me.dirantos.economy.spigot.sidebar.Sidebar;
import me.dirantos.economy.spigot.Utils;
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
    private final EconomyPlugin plugin;
    private final BankManager bankManager;

    public BankUpdateListener(EconomyPlugin plugin, BankManager bankManager) {
        this.plugin = plugin;
        this.bankManager = bankManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);

        for (Player player : Bukkit.getOnlinePlayers()) {
            sidebars.put(player, new Sidebar(DISPLAY_NAME));
        }
    }

    public void loadBanks() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Bank bank = bankManager.loadBank(player);
            Bukkit.getPluginManager().callEvent(new AsyncBankUpdateEvent(bank, bankManager.loadAccounts(bank)));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        sidebars.put(event.getPlayer(), new Sidebar(DISPLAY_NAME));

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
                ChatColor.GREEN +  "Bank balance: " + ChatColor.RESET + Utils.formatMoney(event.getBank().getMoney()), "",
                ChatColor.GREEN + "Your accounts: "));

        event.getAccounts().stream()
                .sorted(Comparator.comparingInt(Account::getAccountNumber))
                .map(Account::getAccountNumber).map(i -> ChatColor.GRAY + "- " + ChatColor.RESET + i)
                .forEach(lines::add);

        double totalBalance = 0;
        for (Account account : event.getAccounts()) {
            totalBalance += account.getBalance();
        }

        lines.addAll(Arrays.asList("", ChatColor.GREEN + "Account balance: " + ChatColor.RESET + Utils.formatMoney(totalBalance)));

        sidebar.setLines(lines);
        sidebar.display(player);
    }

}
