package me.dirantos.economy.spigot.bank.inventories;

import me.dirantos.economy.api.managers.AccountManager;
import me.dirantos.economy.api.models.Account;
import me.dirantos.economy.components.inventory.InventoryBackground;
import me.dirantos.economy.components.inventory.InventoryPage;
import me.dirantos.economy.spigot.EconomyPlugin;
import me.dirantos.economy.spigot.bank.inventoryitems.BankItem;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class BankInventory extends InventoryPage {


    private final Set<Account> accounts;
    private final EconomyPlugin plugin;
    private final AccountManager accountManager;
    private final Player player;

    public BankInventory(Set<Account> accounts, EconomyPlugin plugin, AccountManager accountManager, Player player) {
        super(4, "Bank - " + player.getName(), InventoryBackground.BLACK);
        this.accounts = accounts;
        this.plugin = plugin;
        this.accountManager = accountManager;
        this.player = player;
    }

    @Override
    public void build() {
        int slot = 0;
        for (Account account : accounts.stream().sorted(Comparator.comparingInt(Account::getAccountNumber)).collect(Collectors.toList())) {
            registerItem(slot, new BankItem(plugin, account, player, this));
            slot++;
        }
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }
}
