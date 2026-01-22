package org.creepz.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.creepz.util.database.ban.BanManager;

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
