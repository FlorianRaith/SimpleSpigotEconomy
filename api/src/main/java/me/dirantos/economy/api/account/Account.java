package me.dirantos.economy.api.account;

import java.util.UUID;

public interface Account {

    int getID();

    UUID getOwner();

    double getBalance();

}
