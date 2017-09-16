package me.dirantos.moneymaker.spigot.fetchers;

import me.dirantos.moneymaker.api.Bank;
import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.spigot.impl.BankImpl;
import me.dirantos.moneymaker.spigot.mysql.MySQLConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public final class BankFetcher extends DataFetcher<Bank, UUID> {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `mm_bank` ( `id` INT UNSIGNED NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(36) NOT NULL , `money` DECIMAL(15,2) NOT NULL , PRIMARY KEY (`id`), UNIQUE (`uuid`))";
    private static final String INSERT_DATA = "INSERT INTO `mm_bank` (`uuid`, `money`) VALUES (?,?) ON DUPLICATE KEY UPDATE `money` = VALUES(`money`)";
    private static final String INSERT_MULTIPLE_DATA = "INSERT INTO `mm_bank` (`uuid`, `money`) VALUES $values$ ON DUPLICATE KEY UPDATE `money` = VALUES(`money`)";
    private static final String FETCH_DATA = "SELECT * FROM `mm_bank` WHERE `uuid` = ?";
    private static final String FETCH_MULTIPLE_DATA = "SELECT * FROM `mm_bank` WHERE `uuid` IN $values$";
    private static final String DELETE_DATA = "DELETE FROM `mm_bank` WHERE `uuid` = ?";

    public BankFetcher(MySQLConnectionPool mySQL, MoneyMakerPlugin plugin) {
        super(mySQL, plugin);
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
    public Bank fetchData(UUID uuid) {
        try(Connection connection = getMySQL().getConnection(); PreparedStatement statement = connection.prepareStatement(FETCH_DATA)) {
            statement.setString(1, uuid.toString());
            try(ResultSet result = statement.executeQuery()) {
                if(result.next()) {
                    return new BankImpl(uuid, Double.parseDouble(result.getString("money")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<Bank> fetchMultipleData(Set<UUID> ids) {
        String query = multipleFetchBuilder(FETCH_MULTIPLE_DATA, ids.size());
        getPlugin().log(query);
        try(Connection connection = getMySQL().getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            int i = 1;
            for (UUID id : ids) {
                statement.setString(i, id.toString());
                i++;
            }
            try(ResultSet result = statement.executeQuery()) {
                Set<Bank> bankSet = new HashSet<>();
                while(result.next()) {
                    bankSet.add(new BankImpl(UUID.fromString(result.getString("uuid")), Double.parseDouble(result.getString("money"))));
                }
                return bankSet;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveData(Bank data) {
        try(Connection connection = getMySQL().getConnection(); PreparedStatement statement = connection.prepareStatement(INSERT_DATA)) {
            statement.setString(1, data.getOwner().toString());
            statement.setString(2, String.format(Locale.ENGLISH, "%.2f", data.getMoney()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveMultipleData(Set<Bank> dataSet) {
        String query = multipleInsertBuilder(INSERT_MULTIPLE_DATA, "(?,?)", dataSet.size());
        getPlugin().log(query);
        try(Connection connection = getMySQL().getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            int i = 1;
            for (Bank data : dataSet) {
                statement.setString(i, data.getOwner().toString());
                statement.setString(i+1, String.format(Locale.ENGLISH, "%.2f", data.getMoney()));
                i += 2;
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteData(UUID id) {
        try(Connection connection = getMySQL().getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_DATA)) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
