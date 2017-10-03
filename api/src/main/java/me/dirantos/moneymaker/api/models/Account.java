package me.dirantos.moneymaker.api.models;

import java.util.Set;
import java.util.UUID;

public interface Account extends MMApiModel {

    int getAccountNumber();

    double getBalance();

    void setBalance(double balance);

    UUID getOwner();

    int getTransactionsAmount();

    Set<Integer> getTransactionIDs();

    void addTransaction(Transaction transaction);

}
