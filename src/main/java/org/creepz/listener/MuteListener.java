package org.creepz.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.creepz.util.database.mute.MuteManager;

public class MuteListener implements Listener {

    private static MuteManager muteManager;

    public MuteListener(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (muteManager.isMuted(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(muteManager.getMuteMessage(e.getPlayer().getUniqueId()));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (muteManager.isMuted(e.getPlayer().getUniqueId())) {
            e.getPlayer().sendMessage(muteManager.getMuteMessage(e.getPlayer().getUniqueId()));
        }
    }
}
