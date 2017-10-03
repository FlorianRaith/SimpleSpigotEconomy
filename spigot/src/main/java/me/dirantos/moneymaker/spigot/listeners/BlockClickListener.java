package me.dirantos.moneymaker.spigot.listeners;

import me.dirantos.moneymaker.api.managers.BankManager;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.spigot.bank.AsyncBankInventoryOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;

public class BlockClickListener implements Listener {

    private final MoneyMakerPlugin plugin;

    public BlockClickListener(MoneyMakerPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getClickedBlock().getType() != Material.GOLD_BLOCK) return;

        BankManager bankManager = MoneyMakerAPI.getService().getBankManager();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Set<Account> accounts = bankManager.loadAccounts(bankManager.loadBank(event.getPlayer()));
            Bukkit.getPluginManager().callEvent(new AsyncBankInventoryOpenEvent(accounts, event.getPlayer()));
        });

    }

}
