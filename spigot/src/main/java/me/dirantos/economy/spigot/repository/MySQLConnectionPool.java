package me.dirantos.economy.spigot.repository;

import com.zaxxer.hikari.HikariDataSource;
import me.dirantos.economy.spigot.config.MysqlConnectionConfig;

import java.sql.Connection;
import java.sql.SQLException;

public final class MySQLConnectionPool {

    private final HikariDataSource dataSource;

    public MySQLConnectionPool(String jdbcUrl, String username, String password, int maxPoolSize) {
        this.dataSource = new HikariDataSource();
        dataSource.setMaximumPoolSize(maxPoolSize);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.addDataSourceProperty("user", username);
        dataSource.addDataSourceProperty("password", password);
    }

    public MySQLConnectionPool(MysqlConnectionConfig config) {
        this(config.getJdbcUrl(), config.getUsername(), config.getPassword(), config.getMaxPoolSize());
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        dataSource.close();
    }

}