package me.dirantos.moneymaker.api.account;

import me.dirantos.moneymaker.api.models.Account;

import java.util.UUID;

public interface AccountManager {

    Account createNewAccount(UUID owner, double startBalance);

    Account createNewAccount(UUID owner);

    void deleteAccount(Account account);

    void deleteAccount(int accountNumber);

}
