package me.dirantos.moneymaker.spigot;

import me.dirantos.moneymaker.api.Account;
import me.dirantos.moneymaker.api.Bank;
import me.dirantos.moneymaker.api.Transaction;
import me.dirantos.moneymaker.api.Transfer;
import me.dirantos.moneymaker.api.service.MoneyMakerAPIService;
import me.dirantos.moneymaker.spigot.impl.GodAccount;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class MoneyMakerService implements MoneyMakerAPIService {

    private final MoneyMakerPlugin plugin;
    private final Account godAccount = new GodAccount();


    public MoneyMakerService(MoneyMakerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Account getAccount(int accountNumber) {
        return null;
    }

    @Override
    public Account getGodAccount() {
        return godAccount;
    }

    @Override
    public Transfer addMoney(Account account, int amount) {
        return null;
    }

    @Override
    public Transfer removeMoney(Account account, int amount) {
        return null;
    }

    @Override
    public Bank getBank(Player player) {
        return null;
    }

    @Override
    public Bank getBank(UUID uuid) {
        return null;
    }

}
