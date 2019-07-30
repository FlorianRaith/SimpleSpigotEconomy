package me.dirantos.economy.spigot.repository;

import me.dirantos.economy.api.exception.EconomyException;
import me.dirantos.economy.api.EconomyRepository;
import me.dirantos.economy.api.account.Account;
import me.dirantos.economy.api.bank.Bank;
import me.dirantos.economy.api.transaction.Interest;
import me.dirantos.economy.api.transaction.Transaction;
import me.dirantos.economy.api.transaction.TransactionType;
import me.dirantos.economy.api.transaction.Transfer;
import me.dirantos.economy.spigot.account.AccountImpl;
import me.dirantos.economy.spigot.bank.BankImpl;
import me.dirantos.economy.spigot.transaction.InterestImpl;
import me.dirantos.economy.spigot.transaction.TransactionImpl;
import me.dirantos.economy.spigot.transaction.TransferImpl;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.*;

public class EconomyRepositoryImpl implements EconomyRepository {

    private static final String WALLET_BALANCE_COLUMN = "wallet_balance";
    private static final String BANK_BALANCE_COLUMN = "bank_balance";
    private static final String BANK_UUID_MOST_COLUMN = "bank_uuid_most";
    private static final String BANK_UUID_LEAST_COLUMN = "bank_uuid_least";
    private static final String BALANCE_COLUMN = "balance";
    private static final String ID_COLUMN = "id";
    private static final String ACCOUNT_ID_COLUMN = "account_id";
    private static final String AMOUNT_COLUMN = "amount";
    private static final String DATE_COLUMN = "date";
    private static final String INTEREST_RATE_COLUMN = "interest_rate";
    private static final String SENDER_ACCOUNT_ID_COLUMN = "sender_account_id";
    private static final String RECIPIENT_ACCOUNT_ID_COLUMN = "recipient_account_id";

    private final MySQLConnectionPool connectionPool;
    private final SQLQueries sqlQueries;
    private final Logger logger;

    public EconomyRepositoryImpl(MySQLConnectionPool connectionPool, SQLQueries sqlQueries, Logger logger) {
        this.connectionPool = connectionPool;
        this.sqlQueries = sqlQueries;
        this.logger = logger;
    }

    @Override
    public void initialize() {
        try {
            SQLUtils.runTransaction(connectionPool::getConnection, this::createTablesIfNotExists);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not create tables", e);
        }
    }

    private void createTablesIfNotExists(Connection connection) {
        try(
                PreparedStatement createBanksTable = connection.prepareStatement(sqlQueries.getCreateBanksTable());
                PreparedStatement createAccountsTable = connection.prepareStatement(sqlQueries.getCreateAccountsTable());
                PreparedStatement createTransactionsTable = connection.prepareStatement(sqlQueries.getCreateTransactionsTable())
        ) {
            createBanksTable.executeUpdate();
            createAccountsTable.executeUpdate();
            createTransactionsTable.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not create tables", e);
        }
    }

    @NotNull
    @Override
    public Bank createBank(@NotNull UUID owner) {
        checkNotNull(owner, "owner must not be null");

        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQueries.getInsertBank())
        ) {
            statement.setLong(1, owner.getMostSignificantBits());
            statement.setLong(2, owner.getLeastSignificantBits());
            statement.setBigDecimal(3, BigDecimal.ZERO);
            statement.executeUpdate();

            return new BankImpl(owner, 0, 0);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not create tables", e);
        }

        throw new EconomyException("Could not insert bank to database");
    }

    @NotNull
    @Override
    public Optional<Bank> findBank(@NotNull UUID owner) {
        checkNotNull(owner, "owner must not be null");

        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQueries.getSelectBank())
        ) {
            statement.setLong(1, owner.getMostSignificantBits());
            statement.setLong(2, owner.getLeastSignificantBits());

            try(ResultSet resultSet = statement.executeQuery()) {
                if(!resultSet.next()) return Optional.empty();
                BigDecimal bankBalance = resultSet.getBigDecimal(BANK_BALANCE_COLUMN);
                return Optional.of(new BankImpl(owner, resultSet.getBigDecimal(WALLET_BALANCE_COLUMN).doubleValue(), bankBalance != null ? bankBalance.doubleValue() : 0));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not execute query to find bank in database", e);
        }

        return Optional.empty();
    }

    @Override
    public void updateBank(@NotNull Bank bank) {
        checkNotNull(bank, "bank must not be null");

        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQueries.getUpdateBank())
        ) {
            preparedStatement.setDouble(1, bank.getWalletBalance());
            preparedStatement.setLong(2, bank.getOwner().getMostSignificantBits());
            preparedStatement.setLong(3, bank.getOwner().getLeastSignificantBits());

            preparedStatement.execute();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not update bank in database", e);
        }
    }

    @Override
    public void deleteBank(@NotNull UUID owner) {
        checkNotNull(owner, "owner must not be null");

        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQueries.getDeleteBank())
        ) {
            statement.setLong(1, owner.getMostSignificantBits());
            statement.setLong(2, owner.getLeastSignificantBits());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not delete bank from database", e);
        }
    }

    @NotNull
    @Override
    public Account createAccount(@NotNull UUID owner, double startBalance) {
        checkNotNull(owner, "owner must not be null");

        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQueries.getInsertAccount(), Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setBigDecimal(1, BigDecimal.valueOf(startBalance));
            statement.setLong(2, owner.getMostSignificantBits());
            statement.setLong(3, owner.getLeastSignificantBits());
            int affectedRows = statement.executeUpdate();

            if(affectedRows == 0) throw new EconomyException("Could not insert account in database");
            try(ResultSet resultSet = statement.getGeneratedKeys()) {
                if(!resultSet.next()) throw new EconomyException("Could not insert account in database");
                return new AccountImpl(resultSet.getInt(1), owner, 0);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not insert account in database", e);
        }
        throw new EconomyException("Could not insert account in database");
    }

    @NotNull
    @Override
    public Optional<Account> findAccount(int accountID) {
        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQueries.getSelectAccount())
        ) {
            statement.setInt(1, accountID);

            try(ResultSet resultSet = statement.executeQuery()) {
                if(!resultSet.next()) return Optional.empty();
                UUID owner = new UUID(resultSet.getLong(BANK_UUID_MOST_COLUMN), resultSet.getLong(BANK_UUID_LEAST_COLUMN));
                return Optional.of(new AccountImpl(accountID, owner, resultSet.getBigDecimal(BALANCE_COLUMN).doubleValue()));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not find account in database", e);
        }

        return Optional.empty();
    }

    @NotNull
    @Override
    public Set<Account> findAllBankAccounts(@NotNull UUID owner) {
        checkNotNull(owner, "owner must not be null");

        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQueries.getSelectAccountsByBank())
        ) {
            statement.setLong(1, owner.getMostSignificantBits());
            statement.setLong(2, owner.getLeastSignificantBits());

            try(ResultSet resultSet = statement.executeQuery()) {
                Set<Account> accounts = new HashSet<>();

                while(resultSet.next()) {
                    accounts.add(new AccountImpl(resultSet.getInt(ID_COLUMN), owner, resultSet.getBigDecimal(BALANCE_COLUMN).doubleValue()));
                }

                return Collections.unmodifiableSet(accounts);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not find accounts in database", e);
        }

        throw new EconomyException("Could not find accounts in database");
    }


    @Override
    public void updateAccount(@NotNull Account account) {
        checkNotNull(account, "account must not be null");

        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQueries.getUpdateAccount())
        ) {
            statement.setBigDecimal(1, BigDecimal.valueOf(account.getBalance()));
            statement.setInt(2, account.getID());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not update account in bank", e);
        }
    }

    @Override
    public void deleteAccount(int accountID) {
        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQueries.getDeleteAccount())
        ) {
            statement.setInt(1, accountID);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not delete account from database", e);
        }
    }

    @Override
    public void saveTransaction(@NotNull Bank bank, @NotNull Transaction transaction, @NotNull Account account) {
        checkNotNull(bank, "bank must not be null");
        checkNotNull(transaction, "transaction must not be null");
        checkNotNull(account, "account must not be null");

        try {
            SQLUtils.runTransaction(connectionPool::getConnection, connection -> saveTransaction(connection, bank, transaction, account));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not insert transaction into database", e);
        }
    }

    private void saveTransaction(Connection connection, Bank bank, Transaction transaction, Account account) {
        try(
            PreparedStatement updateBank = connection.prepareStatement(sqlQueries.getUpdateBank());
            PreparedStatement updateAccount = connection.prepareStatement(sqlQueries.getUpdateAccount());
            PreparedStatement createTransaction = connection.prepareStatement(sqlQueries.getInsertTransaction(), Statement.RETURN_GENERATED_KEYS)
        ) {
            updateBank.setBigDecimal(1, BigDecimal.valueOf(bank.getWalletBalance()));
            updateBank.setLong(2, bank.getOwner().getMostSignificantBits());
            updateBank.setLong(3, bank.getOwner().getLeastSignificantBits());
            updateBank.executeUpdate();

            updateAccount.setBigDecimal(1, BigDecimal.valueOf(account.getBalance()));
            updateAccount.setInt(2, account.getID());
            updateAccount.executeUpdate();

            createTransaction.setInt(1, transaction.getAccountID());
            createTransaction.setBigDecimal(2, BigDecimal.valueOf(transaction.getAmount()));
            createTransaction.setString(3, transaction.getType().toString());
            createTransaction.setDate(4, new Date(transaction.getDate().getTime()));
            createTransaction.setDouble(5, 0);
            createTransaction.setInt(6, 0);
            createTransaction.setInt(7, 0);
            int affectedRows = createTransaction.executeUpdate();

            if(affectedRows == 0) return;
            try(ResultSet resultSet = createTransaction.getGeneratedKeys()) {
                if(!resultSet.next()) return;
                ((TransactionImpl) transaction).setID(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not insert transaction into database", e);
        }
    }

    @Override
    public void saveInterest(@NotNull Interest interest, @NotNull Account account) {
        checkNotNull(interest, "interest must not be null");
        checkNotNull(account, "account must not be null");

        try {
            SQLUtils.runTransaction(connectionPool::getConnection, connection -> saveInterest(connection, interest, account));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not insert interest into database", e);
        }
    }

    private void saveInterest(Connection connection, Transaction transaction, Account account) {
        try(
                PreparedStatement createTransaction = connection.prepareStatement(sqlQueries.getInsertTransaction(), Statement.RETURN_GENERATED_KEYS);
                PreparedStatement updateAccount = connection.prepareStatement(sqlQueries.getUpdateAccount())
        ) {
            createTransaction.setInt(1, account.getID());
            createTransaction.setBigDecimal(2, BigDecimal.valueOf(transaction.getAmount()));
            createTransaction.setString(3, transaction.getType().toString());
            createTransaction.setDate(4, new Date(transaction.getDate().getTime()));
            createTransaction.setDouble(5, transaction instanceof Interest ? ((Interest) transaction).getInterestRate() : 0);
            createTransaction.setInt(6, 0);
            createTransaction.setInt(7, 0);
            int affectedRows = createTransaction.executeUpdate();

            updateAccount.setBigDecimal(1, BigDecimal.valueOf(account.getBalance()));
            updateAccount.setInt(2, account.getID());
            updateAccount.executeUpdate();

            if(affectedRows == 0) return;
            try(ResultSet resultSet = createTransaction.getGeneratedKeys()) {
                if(!resultSet.next()) return;
                ((TransactionImpl) transaction).setID(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not insert transaction into database", e);
        }
    }

    @Override
    public void saveTransfer(@NotNull Transfer senderTransfer, @NotNull Transfer receiverTransfer, @NotNull Account sender, @NotNull Account receiver) {
        checkNotNull(senderTransfer, "senderTransfer must not be null");
        checkNotNull(receiverTransfer, "receiverTransfer must not be null");
        checkNotNull(sender, "sender must not be null");
        checkNotNull(receiver, "receiver must not be null");

        try {
            SQLUtils.runTransaction(connectionPool::getConnection, connection -> saveTransfer(connection, senderTransfer, receiverTransfer, sender, receiver));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not insert transfer into database", e);
        }
    }

    private void saveTransfer(Connection connection, Transfer senderTransfer, Transfer receiverTransfer, Account sender, Account receiver) {
        try(
                PreparedStatement createSenderTransfer = connection.prepareStatement(sqlQueries.getInsertTransaction(), Statement.RETURN_GENERATED_KEYS);
                PreparedStatement createReceiverTransfer = connection.prepareStatement(sqlQueries.getInsertTransaction(), Statement.RETURN_GENERATED_KEYS);
                PreparedStatement updateSenderAccount = connection.prepareStatement(sqlQueries.getUpdateAccount());
                PreparedStatement updateReceiverAccount = connection.prepareStatement(sqlQueries.getUpdateAccount())
        ) {
            createSenderTransfer.setInt(1, sender.getID());
            createSenderTransfer.setBigDecimal(2, BigDecimal.valueOf(senderTransfer.getAmount()));
            createSenderTransfer.setString(3, senderTransfer.getType().toString());
            createSenderTransfer.setDate(4, new Date(senderTransfer.getDate().getTime()));
            createSenderTransfer.setDouble(5,0);
            createSenderTransfer.setInt(6, senderTransfer.getSenderAccountID());
            createSenderTransfer.setInt(7, senderTransfer.getRecipientAccountID());
            int affectedRows1 = createSenderTransfer.executeUpdate();

            createReceiverTransfer.setInt(1, receiver.getID());
            createReceiverTransfer.setBigDecimal(2, BigDecimal.valueOf(receiverTransfer.getAmount()));
            createReceiverTransfer.setString(3, receiverTransfer.getType().toString());
            createReceiverTransfer.setDate(4, new Date(receiverTransfer.getDate().getTime()));
            createReceiverTransfer.setDouble(5,0);
            createReceiverTransfer.setInt(6, receiverTransfer.getSenderAccountID());
            createReceiverTransfer.setInt(7, receiverTransfer.getRecipientAccountID());
            int affectedRows2 = createReceiverTransfer.executeUpdate();

            updateSenderAccount.setBigDecimal(1, BigDecimal.valueOf(sender.getBalance()));
            updateSenderAccount.setInt(2, sender.getID());
            updateSenderAccount.executeUpdate();

            updateReceiverAccount.setBigDecimal(1, BigDecimal.valueOf(receiver.getBalance()));
            updateReceiverAccount.setInt(2, receiver.getID());
            updateReceiverAccount.executeUpdate();

            if(affectedRows1 == 0) return;
            try(ResultSet resultSet = createSenderTransfer.getGeneratedKeys()) {
                if(!resultSet.next()) return;
                ((TransferImpl) senderTransfer).setID(resultSet.getInt(1));
            }

            if(affectedRows2 == 0) return;
            try(ResultSet resultSet = createReceiverTransfer.getGeneratedKeys()) {
                if(!resultSet.next()) return;
                ((TransferImpl) receiverTransfer).setID(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not insert transfer into database", e);
        }
    }

    @NotNull
    @Override
    public Set<Transaction> findAllAccountTransactions(int accountID) {
        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQueries.getSelectTransaction())
        ) {
            statement.setInt(1, accountID);

            try(ResultSet resultSet = statement.executeQuery()) {
                Set<Transaction> transactions = new HashSet<>();

                while(resultSet.next()) {
                    TransactionType transactionType = TransactionType.valueOf(resultSet.getString("type"));
                    if(transactionType == TransactionType.INTEREST) {
                        transactions.add(new InterestImpl(
                                resultSet.getInt(ID_COLUMN),
                                resultSet.getInt(ACCOUNT_ID_COLUMN),
                                resultSet.getBigDecimal(AMOUNT_COLUMN).doubleValue(),
                                resultSet.getDate(DATE_COLUMN),
                                resultSet.getDouble(INTEREST_RATE_COLUMN)
                        ));
                    } else if(transactionType == TransactionType.TRANSFER) {
                        transactions.add(new TransferImpl(
                                resultSet.getInt(ID_COLUMN),
                                resultSet.getInt(ACCOUNT_ID_COLUMN),
                                resultSet.getBigDecimal(AMOUNT_COLUMN).doubleValue(),
                                resultSet.getDate(DATE_COLUMN),
                                resultSet.getInt(SENDER_ACCOUNT_ID_COLUMN),
                                resultSet.getInt(RECIPIENT_ACCOUNT_ID_COLUMN)
                        ));
                    } else {
                        transactions.add(new TransactionImpl(
                                resultSet.getInt(ID_COLUMN),
                                resultSet.getInt(ACCOUNT_ID_COLUMN),
                                resultSet.getBigDecimal(AMOUNT_COLUMN).doubleValue(),
                                resultSet.getDate(DATE_COLUMN),
                                transactionType
                        ));
                    }
                }

                return Collections.unmodifiableSet(transactions);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not find transactions in database", e);
        }

        throw new EconomyException("Could not find transactions in database");
    }

    @Override
    public void deleteTransaction(int transactionID) {
        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQueries.getDeleteTransaction())
        ) {
            statement.setInt(1, transactionID);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not delete transaction from database", e);
        }
    }
}
