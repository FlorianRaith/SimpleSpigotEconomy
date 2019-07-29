package me.dirantos.economy.api.account;

import me.dirantos.economy.api.DataModel;

import java.util.Set;
import java.util.UUID;

public interface Account extends DataModel {

    int getAccountNumber();

    double getBalance();

    UUID getOwner();

    int getTransactionsAmount();

    Set<Integer> getTransactionIDs();

}
