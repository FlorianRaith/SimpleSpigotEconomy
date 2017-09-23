package me.dirantos.moneymaker.api.models;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Bank extends MMApiModel {

    UUID getOwner();

    List<Integer> getAccountNumbers();

    /**
     * fetches all accounts from the database
     * @return future
     */
    CompletableFuture<Void> loadAccounts();

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
