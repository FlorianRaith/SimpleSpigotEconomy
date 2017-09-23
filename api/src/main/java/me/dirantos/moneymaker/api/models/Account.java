package me.dirantos.moneymaker.api.models;

import java.util.List;
import java.util.UUID;

public interface Account extends MMApiModel {

    int getAccountNumber();

    double getBalance();

    void setBalance(double balance);

    UUID getOwner();

    int getTransactionsAmount();

    List<Integer> getTransactionIDs();

    void addTransaction(Transaction transaction);

}
