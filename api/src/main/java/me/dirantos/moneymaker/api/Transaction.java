package me.dirantos.moneymaker.api;

import java.util.Date;

public interface Transaction {

    int getID();

    Account getRecipient();

    double getAmount();

    Date getDate();

    TransactionType getType();

}
