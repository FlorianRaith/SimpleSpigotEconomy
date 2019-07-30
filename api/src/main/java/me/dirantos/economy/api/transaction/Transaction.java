package me.dirantos.economy.api.transaction;

import java.util.Date;

public interface Transaction {

    int getID();

    int getAccountID();

    double getAmount();

    Date getDate();

    TransactionType getType();

}
