package me.dirantos.economy.api.managers;

import me.dirantos.economy.api.models.Account;
import me.dirantos.economy.api.models.Bank;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public interface BankManager {

    Bank loadBank(UUID owner);

    Bank loadBank(Player player);

    void setBalance(Bank bank, double amount);

    Set<Account> loadAccounts(Bank bank);

    void deleteBank(Bank bank);

}
