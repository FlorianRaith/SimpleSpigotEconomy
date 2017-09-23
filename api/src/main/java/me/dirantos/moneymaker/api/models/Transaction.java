package me.dirantos.moneymaker.api.models;

import java.util.Date;

public interface Transaction extends MMApiModel {

    int getID();

    int getRecipientAccountNumber();

    double getAmount();

    Date getDate();

    TransactionType getType();

}
