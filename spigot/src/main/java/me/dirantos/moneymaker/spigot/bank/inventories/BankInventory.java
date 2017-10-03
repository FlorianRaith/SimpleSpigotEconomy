package me.dirantos.moneymaker.spigot.bank.inventories;

import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.components.inventory.InventoryBackground;
import me.dirantos.moneymaker.components.inventory.InventoryPage;
import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.spigot.bank.inventoryitems.BankItem;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class BankInventory extends InventoryPage {


    private final Set<Account> accounts;
    private final MoneyMakerPlugin plugin;
    private final Player player;

    public BankInventory(Set<Account> accounts, MoneyMakerPlugin plugin, Player player) {
        super(4, "Bank - " + player.getName(), InventoryBackground.BLACK);
        this.accounts = accounts;
        this.plugin = plugin;
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

}
