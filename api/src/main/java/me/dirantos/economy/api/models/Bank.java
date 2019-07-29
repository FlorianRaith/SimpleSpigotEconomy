package me.dirantos.economy.api.models;

import java.util.List;
import java.util.UUID;

public interface Bank extends MMApiModel {

    UUID getOwner();

    List<Integer> getAccountNumbers();

    /**
     * returns the corresponding player's money
     * @return player's money
     */
    double getMoney();

}
