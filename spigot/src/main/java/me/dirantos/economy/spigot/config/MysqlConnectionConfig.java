package me.dirantos.economy.spigot.config;

public class MysqlConnectionConfig extends AbstractConfig {

    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final int maxPoolSize;

    public MysqlConnectionConfig(ConfigFile config) {
        super(config);
        this.jdbcUrl = getString("mysql.url", "jdbc:mysql://localhost:3306/spigot_economy");
        this.username = getString("mysql.username", "root");
        this.password = getString("mysql.password", "password");
        this.maxPoolSize = getInt("mysql.maxPoolSize", 10);
    }

    public String getJdbcUrl() {
        return jdbcUrl;
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