package me.dirantos.economy.api.transaction;

import me.dirantos.economy.api.DataModel;

import java.util.Date;

public interface Transaction extends DataModel {

    int getID();

    int getRecipientAccountNumber();

    double getAmount();

    Date getDate();

    TransactionType getType();

}
