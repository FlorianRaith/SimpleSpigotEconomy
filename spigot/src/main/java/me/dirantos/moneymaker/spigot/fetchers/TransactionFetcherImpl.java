package me.dirantos.moneymaker.spigot.fetchers;

import me.dirantos.moneymaker.api.fetchers.TransactionFetcher;
import me.dirantos.moneymaker.api.models.Interest;
import me.dirantos.moneymaker.api.models.Transaction;
import me.dirantos.moneymaker.api.models.TransactionType;
import me.dirantos.moneymaker.api.models.Transfer;
import me.dirantos.moneymaker.api.cache.ModelCache;
import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.spigot.models.InterestImpl;
import me.dirantos.moneymaker.spigot.models.TransactionImpl;
import me.dirantos.moneymaker.spigot.models.TransferImpl;
import me.dirantos.moneymaker.spigot.mysql.MySQLConnectionPool;

import java.sql.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class TransactionFetcherImpl extends DataFetcherImpl<Transaction, Integer> implements TransactionFetcher {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `mm_transactions` ( `id` INT UNSIGNED NOT NULL AUTO_INCREMENT , `recipient` INT UNSIGNED NOT NULL , `sender` INT UNSIGNED NULL , `amount` DECIMAL(15,2) NOT NULL , `type` VARCHAR(16) NOT NULL , `date` DATE NOT NULL , `interest_rate` DOUBLE NULL , PRIMARY KEY (`id`))";
    private static final String INSERT_DATA = "INSERT INTO `mm_transactions` (`id`, `recipient`, `sender`, `amount`, `type`, `date`, `interest_rate`) VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE `recipient` = VALUES(`recipient`), `sender` = VALUES(`sender`), `amount` = VALUES(`amount`), `type` = VALUES(`type`), `date` = VALUES(`date`), `interest_rate` = VALUES(`interest_rate`)";
    private static final String INSERT_MULTIPLE_DATA = "INSERT INTO `mm_transactions` (`id`, `recipient`, `sender`, `amount`, `type`, `date`, `interest_rate`) VALUES $values$ ON DUPLICATE KEY UPDATE `recipient` = VALUES(`recipient`), `sender` = VALUES(`sender`), `amount` = VALUES(`amount`), `type` = VALUES(`type`), `date` = VALUES(`date`), `interest_rate` = VALUES(`interest_rate`)";
    private static final String FETCH_DATA = "SELECT * FROM `mm_transactions` WHERE `id` = ?";
    private static final String FETCH_MULTIPLE_DATA = "SELECT * FROM `mm_transactions` WHERE `id` IN $values$";
    private static final String DELETE_DATA = "DELETE FROM `mm_transactions` WHERE `id` = ?";

    public TransactionFetcherImpl(MySQLConnectionPool mySQL, MoneyMakerPlugin plugin, ModelCache cache) {
        super(mySQL, plugin, cache);
    }


    @Override
    public void createTableIfNotExists() {
        try(Connection connection = getMySQL().getConnection(); PreparedStatement statement = connection.prepareStatement(CREATE_TABLE)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Transaction fetchData(Integer id) {
        try(Connection connection = getMySQL().getConnection(); PreparedStatement statement = connection.prepareStatement(FETCH_DATA)) {
            statement.setInt(1, id);
            try(ResultSet result = statement.executeQuery()) {
                if(result.next()) {

                    int recipient = result.getInt("recipient");
                    int sender = result.getInt("sender");
                    double amount = Double.parseDouble(result.getString("amount"));
                    TransactionType type = TransactionType.valueOf(result.getString("type"));
                    Date date = result.getDate("date");
                    double interestRate = result.getDouble("interest_rate");
                    switch (type) {
                        case TRANSFER:
                            return new TransferImpl(id, recipient, amount, date, sender);
                        case INTEREST:
                            return new InterestImpl(id, recipient, amount, date, interestRate);
                        default:
                            return new TransactionImpl(id, recipient, amount, date, type);
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<Transaction> fetchMultipleData(Set<Integer> ids) {
        String query = multipleFetchBuilder(FETCH_MULTIPLE_DATA, ids.size());
        try(Connection connection = getMySQL().getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            int i = 1;
            for (Integer id : ids) {
                statement.setInt(i, id);
                i++;
            }
            try(ResultSet result = statement.executeQuery()) {
                Set<Transaction> transactionSet = new HashSet<>();
                while(result.next()) {

                    int id = result.getInt("id");
                    int recipient = result.getInt("recipient");
                    int sender = result.getInt("sender");
                    double amount = Double.parseDouble(result.getString("amount"));
                    TransactionType type = TransactionType.valueOf(result.getString("type"));
                    Date date = result.getDate("date");
                    double interestRate = result.getDouble("interest_rate");
                    switch (type) {
                        case TRANSFER:
                            transactionSet.add(new TransferImpl(id, recipient, amount, date, sender));
                        case INTEREST:
                            transactionSet.add(new InterestImpl(id, recipient, amount, date, interestRate));
                        default:
                            transactionSet.add(new TransactionImpl(id, recipient, amount, date, type));
                    }

                }
                return transactionSet;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Transaction saveData(Transaction data) {
        try(Connection connection = getMySQL().getConnection(); PreparedStatement statement = connection.prepareStatement(INSERT_DATA, Statement.RETURN_GENERATED_KEYS)) {
            if(data.getID() != -1) statement.setInt(1, data.getID());
            else statement.setNull(1, Types.INTEGER);
            statement.setInt(2, data.getRecipientAccountNumber());
            statement.setInt(3, data instanceof Transfer ? ((Transfer) data).getSenderAccountNumber() : 0);
            statement.setString(4, String.format(Locale.ENGLISH, "%.2f", data.getAmount()));
            statement.setString(5, data.getType().toString());
            statement.setDate(6, new java.sql.Date(data.getDate().getTime()));
            statement.setDouble(7, data instanceof Interest ? ((Interest) data).getInterestRate() : 0);
            int affectedRows = statement.executeUpdate();
            if(affectedRows != 0) {
                try(ResultSet result = statement.getGeneratedKeys()) {
                    if(result.next()) {
                        ((TransactionImpl) data).setId(result.getInt(1));
                        return data;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveMultipleData(Set<Transaction> dataSet) {
        String query = multipleInsertBuilder(INSERT_MULTIPLE_DATA, "(?,?,?,?,?,?,?)", dataSet.size());
        try(Connection connection = getMySQL().getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            int i = 1;
            for (Transaction data : dataSet) {
                statement.setInt(i, data.getID());
                statement.setInt(i+1, data.getRecipientAccountNumber());
                statement.setInt(i+2, data instanceof Transfer ? ((Transfer) data).getSenderAccountNumber() : 0);
                statement.setString(i+3, String.format(Locale.ENGLISH, "%.2f", data.getAmount()));
                statement.setString(i+4, data.getType().toString());
                statement.setDate(i+5, new java.sql.Date(data.getDate().getTime()));
                statement.setDouble(i+6, data instanceof Interest ? ((Interest) data).getInterestRate() : 0);
                i += 7;
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteData(Integer id) {
        try(Connection connection = getMySQL().getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_DATA)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
