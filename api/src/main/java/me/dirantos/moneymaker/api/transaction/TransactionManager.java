package me.dirantos.moneymaker.api.transaction;

import me.dirantos.moneymaker.api.models.Account;
import me.dirantos.moneymaker.api.models.Interest;
import me.dirantos.moneymaker.api.models.Transaction;
import me.dirantos.moneymaker.api.models.Transfer;

public interface TransactionManager {

    Transfer makeTransfer(Account recipient, Account sender, double amount);

    Interest makeInterest(Account recipient, double interestRate);

    Transaction makeWithdrawal(Account recipient, double amount);

    Transaction makeDeposit(Account recipient, double amount);

}
