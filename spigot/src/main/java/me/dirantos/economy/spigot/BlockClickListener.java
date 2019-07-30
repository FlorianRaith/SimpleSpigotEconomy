package me.dirantos.economy.spigot;

import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.spigot.bank.inventory.AsyncBankInventoryOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;

public class BlockClickListener implements Listener {

    private final EconomyPlugin plugin;
    private final EconomyService economyService;

    public BlockClickListener(EconomyPlugin plugin, EconomyService economyService) {
        this.plugin = plugin;
        this.economyService = economyService;
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getClickedBlock().getType() != Material.GOLD_BLOCK) return;

        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Set<Account> accounts = economyService.loadPlayerAccounts(player);
            Bukkit.getPluginManager().callEvent(new AsyncBankInventoryOpenEvent(accounts, player));
        });

        event.setCancelled(true);
    }

}
