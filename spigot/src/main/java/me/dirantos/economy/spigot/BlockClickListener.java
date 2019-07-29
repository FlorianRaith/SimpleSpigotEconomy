package me.dirantos.economy.spigot;

import me.dirantos.economy.api.bank.BankManager;
import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.spigot.EconomyPlugin;
import me.dirantos.economy.spigot.bank.inventory.AsyncBankInventoryOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;

public class BlockClickListener implements Listener {

    private final EconomyPlugin plugin;
    private final BankManager bankManager;

    public BlockClickListener(EconomyPlugin plugin, BankManager bankManager) {
        this.plugin = plugin;
        this.bankManager = bankManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getClickedBlock().getType() != Material.GOLD_BLOCK) return;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Set<Account> accounts = bankManager.loadAccounts(bankManager.loadBank(event.getPlayer()));
            Bukkit.getPluginManager().callEvent(new AsyncBankInventoryOpenEvent(accounts, event.getPlayer()));
        });

        event.setCancelled(true);
    }

}
