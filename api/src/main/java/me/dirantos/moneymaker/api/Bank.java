package me.dirantos.moneymaker.api;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Bank {

    UUID getOwner();

    List<Account> getAccounts();

    /**
     * fetches all accounts from the database
     * @return future
     */
    CompletableFuture<Void> loadAccounts();

    /**
     * returns the sum of the balances of all accounts
     * @return total balance
     */
    double getTotalBalance();

    /**
     * adds to every account their average balance (since the last interest) times the interest rate
     * @param interestRate
     * @return interest-transactions
     */
    List<Interest> receiveInterest(double interestRate);

    /**
     * returns the corresponding player's money
     * @return player's money
     */
    double getMoney();

    void setMoney(double money);

    void addMoney(double amount);

    void removeMoney(double amount);

}
