package me.dirantos.moneymaker.spigot;

import me.dirantos.moneymaker.api.Account;
import me.dirantos.moneymaker.api.Transaction;
import me.dirantos.moneymaker.api.Transfer;
import me.dirantos.moneymaker.api.service.MoneyMakerAPIService;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class MoneyMakerService implements MoneyMakerAPIService {

    private final MoneyMakerPlugin plugin;

    public MoneyMakerService(MoneyMakerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Account getAccount(Player player) {
        return null;
    }

    @Override
    public Account getAccount(UUID uuid) {
        return null;
    }

    @Override
    public Account getAccount(int accountNumber) {
        return null;
    }

    @Override
    public Transfer addMoney(int amount) {
        return null;
    }

    @Override
    public Transfer removeMoney(int amount) {
        return null;
    }

}
