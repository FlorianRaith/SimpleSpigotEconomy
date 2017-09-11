package me.dirantos.moneymaker.api.service;

import me.dirantos.moneymaker.api.Account;
import me.dirantos.moneymaker.api.Transfer;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface MoneyMakerAPIService {

    Account getAccount(Player player);

    Account getAccount(UUID uuid);

    Account getAccount(int accountNumber);

    Transfer addMoney(int amount);

    Transfer removeMoney(int amount);

}
