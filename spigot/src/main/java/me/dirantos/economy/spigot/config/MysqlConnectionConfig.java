package me.dirantos.economy.spigot.config;

import me.dirantos.economy.spigot.config.AbstractConfig;
import me.dirantos.economy.spigot.config.ConfigFile;

public class MysqlConnectionConfig extends AbstractConfig {

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final int maxPoolSize;

    public MysqlConnectionConfig(ConfigFile config) {
        super(config);
        this.host = getString("mysql.host", "localhost");
        this.port = getInt("mysql.port", 3306);
        this.database = getString("mysql.database", "database");
        this.username = getString("mysql.username", "root");
        this.password = getString("mysql.password", "password");
        this.maxPoolSize = getInt("mysql.maxPoolSize", 10);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

}