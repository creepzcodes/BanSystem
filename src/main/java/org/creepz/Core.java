package org.creepz;

import lombok.SneakyThrows;
import org.creepz.command.*;
import org.creepz.listener.BanListener;
import org.creepz.listener.MuteListener;
import org.creepz.util.database.ban.BanManager;
import org.creepz.util.database.DatabaseConnection;
import org.creepz.util.database.mute.MuteManager;
import org.mineacademy.fo.plugin.SimplePlugin;

public class Core extends SimplePlugin {

    private static Core instance;
    private static DatabaseConnection connection;
    private static BanManager banManager;
    private static MuteManager muteManager;

    @SneakyThrows
    @Override
    protected void onPluginStart() {
        instance = this;
        connection = new DatabaseConnection();
        banManager = new BanManager(connection);
        muteManager = new MuteManager(connection);

        connection.connect();

        new BanCommand(banManager).register();
        new UnbanCommand(banManager).register();
        registerEvents(new BanListener(banManager));

        new MuteCommand(muteManager).register();
        new UnmuteCommand(muteManager).register();
        registerEvents(new MuteListener(muteManager));

        new KickCommand().register();

    }

    @Override
    protected void onPluginStop() {
        if (connection != null) {
            connection.disconnect();
        }
    }
}
