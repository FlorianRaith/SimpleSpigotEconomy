package me.dirantos.moneymaker.spigot.commands;

import me.dirantos.moneymaker.api.managers.BankManager;
import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.service.MoneyMakerAPI;
import me.dirantos.moneymaker.components.command.CommandInfo;
import me.dirantos.moneymaker.components.command.SubCommand;
import me.dirantos.moneymaker.components.inventory.InventoryManager;
import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.spigot.bank.inventories.BankInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.Set;

@CommandInfo(name = "open", permission = "moneymaker.cmd.bank.open", usage = "open", description = "opens the bank", playerOnly = true)
public class CmdBankOpen extends SubCommand {

    private final MoneyMakerPlugin plugin;

    public CmdBankOpen(MoneyMakerPlugin plugin) {
        this.plugin = plugin;
        new AccountLoadListener(plugin);
    }

    @Override
    protected void handle(CommandSender sender, String[] args) {
        BankManager bankManager = MoneyMakerAPI.getService().getBankManager();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Set<Account> accounts = bankManager.loadAccounts(bankManager.loadBank((Player) sender));
            Bukkit.getPluginManager().callEvent(new AsyncBankInventoryLoadAccountsEvent(accounts, (Player) sender));
        });
    }

    private static class AccountLoadListener implements Listener {

        private final MoneyMakerPlugin plugin;
        private final InventoryManager inventoryManager;

        public AccountLoadListener(MoneyMakerPlugin plugin) {
            this.plugin = plugin;
            this.inventoryManager = new InventoryManager(plugin);
            Bukkit.getPluginManager().registerEvents(this, plugin);

        }

        @EventHandler
        public void onLoad(AsyncBankInventoryLoadAccountsEvent event) {
            BankInventory inventory = new BankInventory(event.getAccounts(), plugin, event.getPlayer());
            inventoryManager.registerInventory(inventory);
            inventoryManager.openInventory(event.getPlayer(), inventory);
        }

    }

    private static class AsyncBankInventoryLoadAccountsEvent extends Event {

        private static final HandlerList HANDLER_LIST = new HandlerList();

        private final Set<Account> accounts;
        private final Player player;

        public AsyncBankInventoryLoadAccountsEvent(Set<Account> accounts, Player player) {
            super(true);
            this.accounts = accounts;
            this.player = player;
        }

        public Set<Account> getAccounts() {
            return accounts;
        }

        public Player getPlayer() {
            return player;
        }

        public static HandlerList getHandlerList() {
            return HANDLER_LIST;
        }

        @Override
        public HandlerList getHandlers() {
            return HANDLER_LIST;
        }

    }

}
