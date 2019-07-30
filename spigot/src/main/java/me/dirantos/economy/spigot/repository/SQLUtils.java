package me.dirantos.economy.spigot.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

final class SQLUtils {

    private SQLUtils() {}

    /**
     * Run multiple sql statements as part of one transaction
     *
     * @param connectionProvider connection provider which result will be passed to the handler
     * @param handler handler for running multiple sql statements
     * @throws SQLException exception
     */
    static void runTransaction(ConnectionProvider connectionProvider, Consumer<Connection> handler) throws SQLException {
        Connection connection = null;
        boolean autoCommit = false;
        try {
            connection = connectionProvider.get();
            autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false); // begin transaction
            handler.accept(connection);
            connection.commit(); // commit transaction
        } catch(Throwable throwable) {
            if(connection != null) connection.rollback(); // rollback transactions
            throw throwable;
        } finally {
            if(connection != null && autoCommit) {
                connection.setAutoCommit(true); // reset auto commit
            }
        }

    }

    interface ConnectionProvider {
        Connection get() throws SQLException;
    }
}
