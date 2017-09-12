package me.dirantos.moneymaker.spigot.impl;

import me.dirantos.moneymaker.api.Account;
import me.dirantos.moneymaker.api.Interest;
import me.dirantos.moneymaker.api.TransactionType;

import java.util.Date;

public final class InterestImpl extends TransactionImpl implements Interest {

    private final double interestRate;

    public InterestImpl(Account recipient, double amount, Date date, double interestRate) {
        super(recipient, amount, date, TransactionType.INTEREST);
        this.interestRate = interestRate;
    }

    @Override
    public double getInterestRate() {
        return interestRate;
    }

}
