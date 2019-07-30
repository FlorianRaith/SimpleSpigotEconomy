package me.dirantos.economy.spigot;

import me.dirantos.economy.api.EconomyRepository;
import me.dirantos.economy.api.EconomyService;
import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.account.AsyncAccountCreateEvent;
import me.dirantos.economy.api.account.AsyncAccountDeleteEvent;
import me.dirantos.economy.api.account.AsyncAccountUpdateEvent;
import me.dirantos.economy.api.bank.AsyncBankCreateEvent;
import me.dirantos.economy.api.bank.AsyncBankDeleteEvent;
import me.dirantos.economy.api.bank.AsyncBankUpdateEvent;
import me.dirantos.economy.api.bank.Bank;
import me.dirantos.economy.api.transaction.*;
import me.dirantos.economy.spigot.account.AccountImpl;
import me.dirantos.economy.spigot.bank.BankImpl;
import me.dirantos.economy.spigot.transaction.InterestImpl;
import me.dirantos.economy.spigot.transaction.TransactionImpl;
import me.dirantos.economy.spigot.transaction.TransferImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Preconditions.*;

public final class EconomyServiceImpl implements EconomyService {

    private final EconomyRepository repository;

    public EconomyServiceImpl(EconomyRepository repository) {
        this.repository = repository;
    }

    @Override
    public EconomyRepository getRepository() {
        return repository;
    }

    @Override
    public Bank loadBank(UUID player) {
        Optional<Bank> bank = repository.findBank(player);

        if(!bank.isPresent())  {
            Bank createdBank = repository.createBank(player);
            Bukkit.getPluginManager().callEvent(new AsyncBankCreateEvent(createdBank));
            return createdBank;
        }

        return bank.get();
    }

    @Override
    public Bank loadBank(Player player) {
        return loadBank(player.getUniqueId());
    }

    @Override
    public void setWalletBalance(Player player, double amount) {
        setWalletBalance(loadBank(player), amount);
    }

    @Override
    public void setWalletBalance(UUID player, double amount) {
        setWalletBalance(loadBank(player), amount);
    }

    @Override
    public void setWalletBalance(Bank bank, double amount) {
        double oldBalance = bank.getWalletBalance();
        ((BankImpl) bank).setWalletBalance(amount);
        repository.updateBank(bank);
        Bukkit.getPluginManager().callEvent(new AsyncBankUpdateEvent(bank, oldBalance, amount));
    }

    @Override
    public void addWalletBalance(Player player, double amount) {
        addWalletBalance(loadBank(player), amount);
    }

    @Override
    public void addWalletBalance(UUID player, double amount) {
        addWalletBalance(loadBank(player), amount);
    }

    @Override
    public void addWalletBalance(Bank bank, double amount) {
        setWalletBalance(bank, bank.getWalletBalance() + amount);
    }

    @Override
    public void removeWalletBalance(Player player, double amount) {
        removeWalletBalance(loadBank(player), amount);
    }

    @Override
    public void removeWalletBalance(UUID player, double amount) {
        removeWalletBalance(loadBank(player), amount);
    }

    @Override
    public void removeWalletBalance(Bank bank, double amount) {
        setWalletBalance(bank, bank.getWalletBalance() - amount);
    }

    @Override
    public void deleteBank(UUID player) {
        Bukkit.getPluginManager().callEvent(new AsyncBankDeleteEvent(player));
        repository.deleteBank(player);
    }

    @Override
    public void deleteBank(Player player) {
        deleteBank(player.getUniqueId());
    }

    @Override
    public void deleteBank(Bank bank) {
        deleteBank(bank.getOwner());
    }

    @Override
    public Account createNewAccount(UUID player, double startBalance) {
        Account account = repository.createAccount(player, startBalance);
        Bukkit.getPluginManager().callEvent(new AsyncAccountCreateEvent(account));
        return account;
    }

    @Override
    public Account createNewAccount(Player player, double startBalance) {
        return createNewAccount(player.getUniqueId(), startBalance);
    }

    @Override
    public Account createNewAccount(UUID player) {
        return createNewAccount(player, 0);
    }

    @Override
    public Account createNewAccount(Player player) {
        return createNewAccount(player.getUniqueId());
    }

    @Override
    public Optional<Account> loadAccount(int accountID) {
        return repository.findAccount(accountID);
    }

    @Override
    public Set<Account> loadBankAccounts(Bank bank) {
        return loadPlayerAccounts(bank.getOwner());
    }

    @Override
    public Set<Account> loadPlayerAccounts(UUID player) {
        checkNotNull(player, "The player's uuid must not be null");
        return repository.findAllBankAccounts(player);
    }

    @Override
    public Set<Account> loadPlayerAccounts(Player player) {
        checkNotNull(player, "The player must not be null");
        return loadPlayerAccounts(player.getUniqueId());
    }

    @Override
    public void deleteAccount(Account account) {
        checkNotNull(account, "The account must not be null");
        deleteAccount(account.getID());
    }

    @Override
    public void deleteAccount(int accountID) {
        Bukkit.getPluginManager().callEvent(new AsyncAccountDeleteEvent(accountID));
        repository.deleteAccount(accountID);
    }

    @Override
    public Transfer transfer(int senderAccountID, int recipientAccountID, double amount) {
        Optional<Account> sender = loadAccount(senderAccountID);
        Optional<Account> recipient = loadAccount(recipientAccountID);

        checkState(sender.isPresent(), "The sender account must exist");
        checkState(recipient.isPresent(), "The recipient account must exist");

        return transfer(sender.get(), recipient.get(), amount);
    }

    @Override
    public Transfer transfer(Account sender, Account recipient, double amount) {
        checkNotNull(sender, "The sender must not be null");
        checkNotNull(recipient, "The recipient must not be null");
        checkArgument(sender.getBalance() - amount >= 0, String.format("The sender's account balance (%s) must be higher than the transfer amount (%s)", sender.getBalance(), amount));

        Transfer senderTransfer = new TransferImpl(0, sender.getID(), amount, new Date(), sender.getID(), recipient.getID());
        Transfer recipientTransfer = new TransferImpl(0, recipient.getID(), amount, new Date(), sender.getID(), recipient.getID());

        double oldSenderBalance = sender.getBalance();
        double oldRecipientBalance = recipient.getBalance();

        ((AccountImpl) sender).setBalance(sender.getBalance() - amount);
        ((AccountImpl) recipient).setBalance(recipient.getBalance() + amount);

        repository.saveTransfer(senderTransfer, recipientTransfer, sender, recipient);

        Bukkit.getPluginManager().callEvent(new AsyncTransactionEvent(senderTransfer, sender, recipient, sender));
        Bukkit.getPluginManager().callEvent(new AsyncTransactionEvent(recipientTransfer, recipient, recipient, sender));
        Bukkit.getPluginManager().callEvent(new AsyncAccountUpdateEvent(sender, oldSenderBalance, sender.getBalance()));
        Bukkit.getPluginManager().callEvent(new AsyncAccountUpdateEvent(recipient, oldRecipientBalance, recipient.getBalance()));

        return senderTransfer;
    }

    @Override
    public Interest interest(int accountID, double interestRate) {
        Optional<Account> account = loadAccount(accountID);
        checkState(account.isPresent(), "The account must exist");
        return interest(account.get(), interestRate);
    }

    @Override
    public Interest interest(Account recipient, double interestRate) {
        checkNotNull(recipient, "The recipient must not be null");

        double amount = recipient.getBalance() * interestRate;
        Interest interest = new InterestImpl(0, recipient.getID(), amount, new Date(), interestRate);
        double oldBalance = recipient.getBalance();
        ((AccountImpl) recipient).setBalance(recipient.getBalance() + amount);

        repository.saveInterest(interest, recipient);

        Bukkit.getPluginManager().callEvent(new AsyncTransactionEvent(interest, recipient));
        Bukkit.getPluginManager().callEvent(new AsyncAccountUpdateEvent(recipient, oldBalance, recipient.getBalance()));

        return interest;
    }

    @Override
    public Transaction withdrawal(int accountID, double amount) {
        Optional<Account> account = loadAccount(accountID);
        checkState(account.isPresent(), "The account must exist");
        return withdrawal(loadBank(account.get().getOwner()), account.get(), amount);
    }

    @Override
    public Transaction withdrawal(Account recipient, double amount) {
        return withdrawal(loadBank(recipient.getOwner()), recipient, amount);
    }

    @Override
    public Transaction withdrawal(Bank bank, int accountID, double amount) {
        Optional<Account> account = loadAccount(accountID);
        checkState(account.isPresent(), "The account must exist");
        return withdrawal(bank, account.get(), amount);
    }

    @Override
    public Transaction withdrawal(Bank bank, Account recipient, double amount) {
        checkNotNull(recipient, "The recipient must not be null");
        checkArgument(recipient.getBalance() - amount >= 0, String.format("The recipient's account balance (%s) must be equal to or higher than the withdrawal amount (%s)", recipient.getBalance(), amount));

        double oldWalletBalance = bank.getWalletBalance();
        ((BankImpl) bank).setWalletBalance(bank.getWalletBalance() + amount);
        ((BankImpl) bank).setBankBalance(bank.getBankBalance() - amount);
        double oldAccountBalance = recipient.getBalance();
        ((AccountImpl) recipient).setBalance(recipient.getBalance() - amount);
        Transaction transaction = new TransactionImpl(0, recipient.getID(), amount, new Date(), TransactionType.WITHDRAWAL);

        repository.saveTransaction(bank, transaction, recipient);

        Bukkit.getPluginManager().callEvent(new AsyncTransactionEvent(transaction, recipient));
        Bukkit.getPluginManager().callEvent(new AsyncAccountUpdateEvent(recipient, oldAccountBalance, recipient.getBalance()));
        Bukkit.getPluginManager().callEvent(new AsyncBankUpdateEvent(bank, oldWalletBalance, bank.getWalletBalance()));

        return transaction;
    }

    @Override
    public Transaction deposit(int accountID, double amount) {
        Optional<Account> account = loadAccount(accountID);
        checkState(account.isPresent(), "The account must exist");
        return deposit(loadBank(account.get().getOwner()), account.get(), amount);
    }

    @Override
    public Transaction deposit(Account recipient, double amount) {
        return deposit(loadBank(recipient.getOwner()), recipient, amount);
    }

    @Override
    public Transaction deposit(Bank bank, int accountID, double amount) {
        Optional<Account> account = loadAccount(accountID);
        checkState(account.isPresent(), "The account must exist");
        return deposit(bank, account.get(), amount);
    }

    @Override
    public Transaction deposit(Bank bank, Account recipient, double amount) {
        checkNotNull(recipient, "The recipient must not be null");
        checkArgument(bank.getWalletBalance() - amount >= 0, String.format("The recipient's wallet balance (%s) must be equal to or higher than the withdrawal amount (%s)", bank.getWalletBalance(), amount));

        double oldWalletBalance = bank.getWalletBalance();
        ((BankImpl) bank).setWalletBalance(bank.getWalletBalance() - amount);
        ((BankImpl) bank).setBankBalance(bank.getBankBalance() + amount);
        double oldAccountBalance = recipient.getBalance();
        ((AccountImpl) recipient).setBalance(recipient.getBalance() + amount);
        Transaction transaction = new TransactionImpl(0, recipient.getID(), amount, new Date(), TransactionType.DEPOSIT);

        repository.saveTransaction(bank, transaction, recipient);

        Bukkit.getPluginManager().callEvent(new AsyncTransactionEvent(transaction, recipient));
        Bukkit.getPluginManager().callEvent(new AsyncAccountUpdateEvent(recipient, oldAccountBalance, recipient.getBalance()));
        Bukkit.getPluginManager().callEvent(new AsyncBankUpdateEvent(bank, oldWalletBalance, bank.getWalletBalance()));

        return transaction;
    }

    @Override
    public Optional<Transaction> loadTransaction(int transactionID) {
        return Optional.empty();
    }

    @Override
    public Set<Transaction> loadAccountTransactions(int accountID) {
        return repository.findAllAccountTransactions(accountID);
    }

    @Override
    public Set<Transaction> loadAccountTransactions(Account account) {
        return repository.findAllAccountTransactions(account.getID());
    }

    @Override
    public void deleteTransaction(int transactionID) {
        repository.deleteTransaction(transactionID);
    }

    @Override
    public void deleteTransaction(Transaction transaction) {
        repository.deleteTransaction(transaction.getID());
    }

}
