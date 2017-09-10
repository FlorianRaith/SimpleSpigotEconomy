package me.dirantos.moneymaker.api;

import java.util.List;
import java.util.UUID;

public interface Bank {

    UUID getOwner();

    List<Account> getAccounts();

    /**
     * returns the sum of the balances of all accounts
     * @return total balance
     */
    double getTotalBalance();

    List<Interest> recieveInterest(float interestRate);

    /**
     * returns the corresponding player's money
     * @return player's money
     */
    double getMoney();

}
