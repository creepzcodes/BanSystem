package org.creepz.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.creepz.util.database.BanManager;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Arrays;
import java.util.List;

public class BanCommand extends SimpleCommand {

    private final BanManager banManager;

    public BanCommand(BanManager banManager) {
        super("ban");
        this.banManager = banManager;
        setPermission("command.ban");
    }

    @Override
    protected void onCommand() {
        Player player = getPlayer();
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            tellError("Spieler nicht gefunden");
        }

        if (banManager.isBanned(target.getUniqueId())) {
            tellError("Spieler ist bereits gebannt!");
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
        String bannedBy = sender.getName();

        banManager.ban(
                target.getUniqueId(),
                reason,
                bannedBy,
                duration
        );

        tellSuccess("Der Spieler '"+target.getName()+"' wurde erfolgreich gebannt!");
        tellSuccess("Grund: '"+reason+"'");
        tellSuccess("Dauer: '"+duration+"'");

        target.kickPlayer(banManager.getBanMessage(target.getUniqueId()));
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
