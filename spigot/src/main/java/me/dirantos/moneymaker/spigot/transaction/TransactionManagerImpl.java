package me.dirantos.moneymaker.spigot.transaction;

import me.dirantos.moneymaker.api.fetchers.AccountFetcher;
import me.dirantos.moneymaker.api.fetchers.TransactionFetcher;
import me.dirantos.moneymaker.api.models.*;
import me.dirantos.moneymaker.api.transaction.TransactionManager;
import me.dirantos.moneymaker.spigot.models.AccountImpl;
import org.apache.commons.lang.Validate;

public final class TransactionManagerImpl implements TransactionManager {

    private final TransactionFactory factory = new TransactionFactory();
    private final TransactionFetcher transactionFetcher;
    private final AccountFetcher accountFetcher;

    public TransactionManagerImpl(TransactionFetcher transactionFetcher, AccountFetcher accountFetcher) {
        this.transactionFetcher = transactionFetcher;
        this.accountFetcher = accountFetcher;
    }

    @Override
    public Transfer makeTransfer(Account recipient, Account sender, double amount) {
        Transfer transfer = factory.createTransfer(recipient.getAccountNumber(), sender.getAccountNumber(), amount);
        Validate.isTrue(recipient.getAccountNumber() == transfer.getRecipientAccountNumber(), "The given recipient account does not correspond with the transaction recipient account-number");
        Validate.isTrue(sender.getAccountNumber() == transfer.getSenderAccountNumber(), "The given sender account does not correspond with the transaction sender account-number");

        if(sender.getBalance() - transfer.getAmount() < 0) throw new IllegalStateException("The sender has not enough money");
        sender.setBalance(sender.getBalance() - transfer.getAmount());
        recipient.setBalance(recipient.getBalance() + transfer.getAmount());

        Transaction validated = transactionFetcher.saveData(transfer);

        recipient.addTransaction(validated);
        sender.addTransaction(validated);
        accountFetcher.saveData(recipient);
        accountFetcher.saveData(sender);

        return (Transfer) validated;
    }

    @Override
    public Interest makeInterest(Account recipient, double interestRate) {
        Interest interest = factory.createInterest(recipient.getAccountNumber(), 0, interestRate);
        Validate.isTrue(recipient.getAccountNumber() == interest.getRecipientAccountNumber(), "The given recipient account does not correspond with the transaction recipient account-number");

        double averageBalances = ((AccountImpl) recipient).getBalanceChanges().stream().reduce(0.0, Double::sum) / 2.0;
        recipient.setBalance(recipient.getBalance() + (averageBalances * interest.getInterestRate()));
        recipient.addTransaction(interest);

        Transaction validated = transactionFetcher.saveData(interest);

        recipient.addTransaction(validated);
        accountFetcher.saveData(recipient);

        return (Interest) validated;
    }

    @Override
    public Transaction makeWithdrawal(Account recipient, double amount) {
        Transaction transaction = factory.createWithdrawal(recipient.getAccountNumber(), amount);
        Validate.isTrue(recipient.getAccountNumber() == transaction.getRecipientAccountNumber(), "The given recipient account does not correspond with the transaction recipient account-number");

        recipient.setBalance(recipient.getBalance() + transaction.getAmount());

        Transaction validated = transactionFetcher.saveData(transaction);

        recipient.addTransaction(validated);
        accountFetcher.saveData(recipient);

        return validated;
    }

    @Override
    public Transaction makeDeposit(Account recipient, double amount) {
        Transaction transaction = factory.createDeposit(recipient.getAccountNumber(), amount);
        Validate.isTrue(recipient.getAccountNumber() == transaction.getRecipientAccountNumber(), "The given recipient account does not correspond with the transaction recipient account-number");

        if(recipient.getBalance() - transaction.getAmount() < 0) recipient.setBalance(0);
        else recipient.setBalance(recipient.getBalance() - transaction.getAmount());

        Transaction validated = transactionFetcher.saveData(transaction);

        recipient.addTransaction(validated);
        accountFetcher.saveData(recipient);

        return validated;
    }

}
