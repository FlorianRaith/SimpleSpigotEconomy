package me.dirantos.economy.api.transaction;

public interface Transfer extends Transaction {

    int getSenderAccountID();

    int getRecipientAccountID();

}
