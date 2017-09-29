package me.dirantos.moneymaker.spigot.fetchers;

import me.dirantos.moneymaker.api.fetchers.BankFetcher;
import me.dirantos.moneymaker.api.models.Bank;
import me.dirantos.moneymaker.api.cache.ModelCache;
import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.spigot.models.BankImpl;
import me.dirantos.moneymaker.spigot.mysql.MySQLConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public final class BankFetcherImpl extends DataFetcherImpl<Bank, UUID> implements BankFetcher {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `mm_bank` ( `id` INT UNSIGNED NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(36) NOT NULL , `money` DECIMAL(15,2) NOT NULL , `account_numbers` VARCHAR(255) NOT NULL , PRIMARY KEY (`id`), UNIQUE (`uuid`))";
    private static final String INSERT_DATA = "INSERT INTO `mm_bank` (`uuid`, `money`, `account_numbers`) VALUES (?,?,?) ON DUPLICATE KEY UPDATE `money` = VALUES(`money`), `account_numbers` = VALUES(`account_numbers`)";
    private static final String INSERT_MULTIPLE_DATA = "INSERT INTO `mm_bank` (`uuid`, `money`, `account_numbers`) VALUES $values$ ON DUPLICATE KEY UPDATE `money` = VALUES(`money`), `account_numbers` = VALUES(`account_numbers`)";
    private static final String FETCH_DATA = "SELECT * FROM `mm_bank` WHERE `uuid` = ?";
    private static final String FETCH_MULTIPLE_DATA = "SELECT * FROM `mm_bank` WHERE `uuid` IN $values$";
    private static final String DELETE_DATA = "DELETE FROM `mm_bank` WHERE `uuid` = ?";

    public BankFetcherImpl(MySQLConnectionPool mySQL, MoneyMakerPlugin plugin, ModelCache cache) {
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
    public Bank fetchData(UUID uuid) {
        try(Connection connection = getMySQL().getConnection(); PreparedStatement statement = connection.prepareStatement(FETCH_DATA)) {
            statement.setString(1, uuid.toString());
            try(ResultSet result = statement.executeQuery()) {
                if(result.next()) {
                    String[] arr = result.getString("account_numbers").split(",");
                    List<Integer> accountNumbers = new ArrayList<>();
                    for (String s : arr) {
                        int x = -1;
                        try {
                            x = Integer.parseInt(s);
                        } catch (NumberFormatException e) {}
                        if(x != -1) accountNumbers.add(x);
                    }
                    Bank bank = new BankImpl(uuid, accountNumbers, Double.parseDouble(result.getString("money")));
                    getCache().getBankCache().add(uuid, bank);
                    return bank;
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
        try(Connection connection = getMySQL().getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            int i = 1;
            for (UUID id : ids) {
                statement.setString(i, id.toString());
                i++;
            }
            try(ResultSet result = statement.executeQuery()) {
                Set<Bank> bankSet = new HashSet<>();
                while(result.next()) {
                    String[] arr = result.getString("account_numbers").split(",");
                    List<Integer> accountNumbers = new ArrayList<>();
                    for (String s : arr) {
                        int x = -1;
                        try {
                            x = Integer.parseInt(s);
                        } catch (NumberFormatException e) {}
                        if(x != -1) accountNumbers.add(x);
                    }
                    UUID uuid  = UUID.fromString(result.getString("uuid"));
                    Bank bank = new BankImpl(uuid, accountNumbers, Double.parseDouble(result.getString("money")));
                    getCache().getBankCache().add(uuid, bank);
                    bankSet.add(bank);
                }
                return bankSet;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Bank saveData(Bank data) {
        try(Connection connection = getMySQL().getConnection(); PreparedStatement statement = connection.prepareStatement(INSERT_DATA)) {
            statement.setString(1, data.getOwner().toString());
            statement.setString(2, String.format(Locale.ENGLISH, "%.2f", data.getMoney()));
            statement.setString(3, data.getAccountNumbers().stream().map(Object::toString).collect(Collectors.joining(",")));
            statement.executeUpdate();
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveMultipleData(Set<Bank> dataSet) {
        String query = multipleInsertBuilder(INSERT_MULTIPLE_DATA, "(?,?,?)", dataSet.size());
        try(Connection connection = getMySQL().getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            int i = 1;
            for (Bank data : dataSet) {
                statement.setString(i, data.getOwner().toString());
                statement.setString(i+1, String.format(Locale.ENGLISH, "%.2f", data.getMoney()));
                statement.setString(i+2, data.getAccountNumbers().stream().map(Object::toString).collect(Collectors.joining(",")));
                i += 3;
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
