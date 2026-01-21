package org.creepz;

import lombok.SneakyThrows;
import org.creepz.util.database.DatabaseConnection;
import org.mineacademy.fo.plugin.SimplePlugin;

public class Core extends SimplePlugin {

    private static Core instance;
    private static DatabaseConnection connection;

    @SneakyThrows
    @Override
    protected void onPluginStart() {
        instance = this;
        connection = new DatabaseConnection();

        connection.connect();

    }

    @Override
    protected void onPluginStop() {
        if (connection != null) {
            connection.disconnect();
        }
    }
}
