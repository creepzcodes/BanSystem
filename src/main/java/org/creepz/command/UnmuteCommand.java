package org.creepz.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.creepz.util.database.mute.MuteManager;
import org.mineacademy.fo.command.SimpleCommand;

public class UnmuteCommand extends SimpleCommand {

    private static MuteManager muteManager;

    public UnmuteCommand(MuteManager muteManager) {
        super("unmute");
        setPermission("command.unmute");
        this.muteManager = muteManager;
    }

    @Override
    protected void onCommand() {
        Player player = getPlayer();
        Player target = Bukkit.getOfflinePlayer(args[0]).getPlayer();

        if (target == null) {
            tellError("Dieser Spieler konnte nicht gefunden werden!");
            return;
        }

        if (!muteManager.isMuted(target.getUniqueId())) {
            tellError("Dieser Spieler ist nicht gemuted!");
            return;
        }

        muteManager.unmute(target.getUniqueId());
        tellSuccess("Der Spieler '"+target.getName()+"' wurde entmuted");
        target.sendMessage("Du wurdest entmuted!");
    }
}
