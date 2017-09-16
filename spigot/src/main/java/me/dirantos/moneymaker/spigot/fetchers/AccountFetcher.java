package me.dirantos.moneymaker.spigot.fetchers;

import me.dirantos.moneymaker.api.Account;
import me.dirantos.moneymaker.spigot.MoneyMakerPlugin;
import me.dirantos.moneymaker.spigot.mysql.MySQLConnectionPool;

import java.util.Set;

public final class AccountFetcher extends DataFetcher<Account, Integer> {

    private static final String CREATE_TABLE = "";
    private static final String INSERT_DATA = "";
    private static final String INSERT_MULTIPLE_DATA = "";
    private static final String FETCH_DATA = "";
    private static final String FETCH_MULTIPLE_DATA = "";
    private static final String DELETE_DATA = "";

    public AccountFetcher(MySQLConnectionPool mySQL, MoneyMakerPlugin plugin) {
        super(mySQL, plugin);
    }

    @Override
    public void createTableIfNotExists() {

    }

    @Override
    public Account fetchData(Integer id) {
        return null;
    }

    @Override
    public Set<Account> fetchMultipleData(Set<Integer> ids) {
        return null;
    }

    @Override
    public void saveData(Account data) {

    }

    @Override
    public void saveMultipleData(Set<Account> data) {

    }

    @Override
    public void deleteData(Integer id) {

    }

}
