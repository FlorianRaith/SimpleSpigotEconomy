package me.dirantos.moneymaker.api.models;

import java.util.Date;

public interface Transaction {

    int getID();

    int getRecipientAccountNumber();

    double getAmount();

    Date getDate();

    TransactionType getType();

}
