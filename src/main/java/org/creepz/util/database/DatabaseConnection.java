package org.creepz.util.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
@NoArgsConstructor(force = true)
public class DatabaseConnection {

    private HikariDataSource dataSource;

    private final String host = "localhost";
    private final String port = "3309";
    private final String database = "bauhd";
    private final String user = "root";
    private final String password = "";

    public void connect() {
        if (dataSource != null && !dataSource.isClosed()) return;

        HikariConfig config = new HikariConfig();

        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database
                + "?useSSL=false&autoReconnect=true&allowPublicKeyRetrieval=true";

        config.setJdbcUrl(jdbcUrl);
        config.setUsername(user);
        config.setPassword(password);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(10000);
        config.setLeakDetectionThreshold(20000);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        this.dataSource = new HikariDataSource(config);

        try (Connection connection = getConnection()) {
            createTables(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            connect();
        }
        return dataSource.getConnection();
    }

    private void createTables(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS bans (" +
                        "uuid VARCHAR(36) PRIMARY KEY, " +
                        "reason TEXT NOT NULL, " +
                        "banned_by VARCHAR(100) NOT NULL, " +
                        "start_time BIGINT NOT NULL," +
                        "end_time BIGINT NOT NULL"+
                        ")"
        )) {
            statement.executeUpdate();
        }

        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS mutes (" +
                        "uuid VARCHAR(36) PRIMARY KEY, " +
                        "reason TEXT NOT NULL, " +
                        "muted_by VARCHAR(100) NOT NULL, " +
                        "start_time BIGINT NOT NULL," +
                        "end_time BIGINT NOT NULL"+
                        ")"
        )) {
            statement.executeUpdate();
        }
    }

    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public boolean isConnected() {
        return dataSource != null && !dataSource.isClosed();
    }
}
