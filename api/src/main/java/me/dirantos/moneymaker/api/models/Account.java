package me.dirantos.moneymaker.api.models;

import java.util.List;
import java.util.UUID;

public interface Account {

    int getAccountNumber();

    double getBalance();

    UUID getOwner();

    int getTransactionsAmount();

    List<Integer> getTransactionIDs();

}
