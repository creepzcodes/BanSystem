package org.creepz.listener;

import io.papermc.paper.connection.PlayerLoginConnection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.creepz.util.database.Ban;
import org.creepz.util.database.BanManager;

import java.util.UUID;

public class BanListener implements Listener {

    private final BanManager banManager;

    public BanListener(BanManager banManager) {
        this.banManager = banManager;
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();

        if (banManager.isBanned(uuid)) {
            e.disallow(
                    AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                    banManager.getBanMessage(uuid)
            );
        }
    }

}
