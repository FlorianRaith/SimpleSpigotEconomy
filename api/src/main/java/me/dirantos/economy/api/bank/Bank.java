package me.dirantos.economy.api.bank;

import java.util.UUID;

public interface Bank {

    /**
     * returns the uuid of the owner of this bank
     * @return owner's uuid
     */
    UUID getOwner();

    /**
     * returns the money the player currently has in his wallet
     * @return wallet balance
     */
    double getWalletBalance();

    /**
     * returns the sum of money on all the player's accounts
     * @return bank balance
     */
    double getBankBalance();

}
