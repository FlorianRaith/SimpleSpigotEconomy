package me.dirantos.moneymaker.api.models;

import java.util.Set;
import java.util.UUID;

public interface Account extends MMApiModel {

    int getAccountNumber();

    double getBalance();

    UUID getOwner();

    int getTransactionsAmount();

    Set<Integer> getTransactionIDs();

}
