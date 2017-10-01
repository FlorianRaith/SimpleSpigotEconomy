package me.dirantos.moneymaker.spigot.mysql;

import com.zaxxer.hikari.HikariDataSource;
import me.dirantos.moneymaker.spigot.configs.MysqlConnectionConfig;

import java.sql.Connection;
import java.sql.SQLException;

public final class MySQLConnectionPool {

    private final HikariDataSource dataSource;

    public MySQLConnectionPool(String host, int port, String database, String username, String password, int maxPoolSize) {
        this.dataSource = new HikariDataSource();
        dataSource.setMaximumPoolSize(maxPoolSize);
        dataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        dataSource.addDataSourceProperty("serverName", host);
        dataSource.addDataSourceProperty("port", port);
        dataSource.addDataSourceProperty("databaseName", database);
        dataSource.addDataSourceProperty("user", username);
        dataSource.addDataSourceProperty("password", password);
    }

    public MySQLConnectionPool(MysqlConnectionConfig config) {
        this(config.getHost(), config.getPort(), config.getDatabase(), config.getUsername(), config.getPassword(), config.getMaxPoolSize());
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        dataSource.close();
    }

}
