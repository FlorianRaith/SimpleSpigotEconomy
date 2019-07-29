package me.dirantos.economy.api.bank;

import me.dirantos.economy.api.DataModel;

import java.util.List;
import java.util.UUID;

public interface Bank extends DataModel {

    UUID getOwner();

    List<Integer> getAccountNumbers();

    /**
     * returns the corresponding player's money
     * @return player's money
     */
    double getMoney();

}
