package org.creepz.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.creepz.util.database.mute.MuteManager;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Arrays;
import java.util.List;

public class MuteCommand extends SimpleCommand {

    private static MuteManager muteManager;

    public MuteCommand(MuteManager muteManager) {
        super("mute");
        setPermission("command.mute");
        this.muteManager = muteManager;
    }

    @Override
    protected void onCommand() {

        Player player = getPlayer();
        Player target = Bukkit.getOfflinePlayer(args[0]).getPlayer();
        if (target == null) {
            tellError("Dieser Spieler wurde nicht gefunden!");
            return;
        }

        if (muteManager.isMuted(target.getUniqueId())) {
            tellError("Dieser Spieler ist bereits gemuted!");
            tellError(muteManager.getMuteMessage(target.getUniqueId()));
            return;
        }

        long duration;
        if (args[1].equals("perm")) {
            duration = -1;
        } else {
            duration = parseTime(args[1]);
            if (duration <= 0) {
                tellError("Ungültige Zeitangabe!");
                return;
            }
        }

        String reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        String mutedBy = sender.getName();

        muteManager.mute(
                target.getUniqueId(),
                reason,
                mutedBy,
                duration
        );

        tellSuccess("------------------------------");
        tellSuccess("§5");
        tellSuccess("Der Spieler' "+target.getName()+"' wurde erfolgreich gemuted!");
        tellSuccess("Dauer: '"+muteManager.formatTime(duration)+"'");
        tellSuccess("Grund: '"+reason+"'");
        tellSuccess("§5");
        tellSuccess("------------------------------");

        target.sendMessage(muteManager.getMuteMessage(target.getUniqueId()));
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1) {
            return completeLastWordPlayerNames();
        }

        if (args.length == 2) {
            return List.of("1m", "1h", "1d", "perm");
        }

        if (args.length == 3) {
            return List.of("Sei nicht so frech", "Beleidigung");
        }


        return super.tabComplete();
    }

    private long parseTime(String input) {
        try {
            long value = Long.parseLong(input.substring(0, input.length() - 1));
            char unit = input.charAt(input.length() - 1);

            switch (unit) {
                case 's': return value * 1000;
                case 'm': return value * 60 * 1000;
                case 'h': return value * 60 * 60 * 1000;
                case 'd': return value * 24 * 60 * 60 * 1000;
                default: return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }
}
