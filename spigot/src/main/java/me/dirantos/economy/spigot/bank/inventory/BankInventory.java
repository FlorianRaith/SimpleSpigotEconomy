package me.dirantos.economy.spigot.bank.inventory;

import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.spigot.inventory.InventoryBackground;
import me.dirantos.economy.spigot.inventory.InventoryPage;
import me.dirantos.economy.spigot.EconomyPlugin;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class BankInventory extends InventoryPage {


    private final Set<Account> accounts;
    private final EconomyPlugin plugin;
    private final EconomyService economyService;
    private final Player player;

    public BankInventory(Set<Account> accounts, EconomyPlugin plugin, EconomyService economyService, Player player) {
        super(4, "Bank - " + player.getName(), InventoryBackground.BLACK);
        this.accounts = accounts;
        this.plugin = plugin;
        this.economyService = economyService;
        this.player = player;
    }

    @Override
    public void build() {
        int slot = 0;
        for (Account account : accounts.stream().sorted(Comparator.comparingInt(Account::getID)).collect(Collectors.toList())) {
            registerItem(slot, new BankItem(plugin, account, player, this));
            slot++;
        }
    }

    public EconomyService getEconomyService() {
        return economyService;
    }
}
