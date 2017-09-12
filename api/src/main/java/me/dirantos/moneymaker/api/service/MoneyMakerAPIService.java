package me.dirantos.moneymaker.api.service;

import me.dirantos.moneymaker.api.Account;
import me.dirantos.moneymaker.api.Bank;
import me.dirantos.moneymaker.api.Transfer;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface MoneyMakerAPIService {

    Account getAccount(int accountNumber);

    /* the god-account is just a account with an infinity money source */
    Account getGodAccount();

    /* transfers money from the god-account to the given account */
    Transfer addMoney(Account account, int amount);

    /* transfers money from the given account to the god-account */
    Transfer removeMoney(Account account, int amount);

    Bank getBank(Player player);

    Bank getBank(UUID uuid);

}