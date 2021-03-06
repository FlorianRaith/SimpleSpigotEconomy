package me.dirantos.economy.spigot.bank;

import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.api.account.AsyncAccountUpdateEvent;
import me.dirantos.economy.api.bank.AsyncBankUpdateEvent;
import me.dirantos.economy.api.bank.Bank;
import me.dirantos.economy.spigot.EconomyPlugin;
import me.dirantos.economy.spigot.Utils;
import me.dirantos.economy.spigot.sidebar.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class BankUpdateListener implements Listener {

    private static final String DISPLAY_NAME = ChatColor.DARK_GREEN + "-=[ " + ChatColor.GREEN + "Bank" + ChatColor.DARK_GREEN + " ]=-";

    private final Map<Player, Sidebar> sidebars = new HashMap<>();
    private final EconomyPlugin plugin;
    private final EconomyService economyService;

    public BankUpdateListener(EconomyPlugin plugin, EconomyService economyService) {
        this.plugin = plugin;
        this.economyService = economyService;

        for (Player player : Bukkit.getOnlinePlayers()) {
            sidebars.put(player, new Sidebar(DISPLAY_NAME));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        sidebars.put(player, new Sidebar(DISPLAY_NAME));
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Bank bank = economyService.loadBank(event.getPlayer());
            updateSidebar(player, bank);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        sidebars.remove(event.getPlayer());
    }


    @EventHandler
    public void onBankUpdate(AsyncBankUpdateEvent event) {
        Player player = Bukkit.getPlayer(event.getBank().getOwner());
        if(player == null) return;

        Bank bank = event.getBank();
        updateSidebar(player, bank);
    }

    @EventHandler
    public void onAccountUpdate(AsyncAccountUpdateEvent event) {
        Player player = Bukkit.getPlayer(event.getAccount().getOwner());
        if(player == null) return;

        Bank bank = economyService.loadBank(event.getAccount().getOwner());
        ((BankImpl) bank).setBankBalance(bank.getBankBalance() + (event.getNewAmount() - event.getOldAmount()));

        updateSidebar(player, bank);
    }

    private void updateSidebar(Player player, Bank bank) {
        Sidebar sidebar = sidebars.get(player);

        List<String> lines = new ArrayList<>(Arrays.asList("",
                ChatColor.GREEN + "Wallet balance: " + ChatColor.RESET + Utils.formatMoney(bank.getWalletBalance()),
                "",
                ChatColor.GREEN + "Bank balance: " + ChatColor.RESET + Utils.formatMoney(bank.getBankBalance())
        ));

        sidebar.setLines(lines);
        sidebar.display(player);
    }

}
