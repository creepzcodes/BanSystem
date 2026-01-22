package org.creepz.command;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.creepz.util.database.ban.Ban;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class KickCommand extends SimpleCommand {

    public KickCommand() {
        super("kick");
        setPermission("command.kick");
    }

    @Override
    protected void onCommand() {
        Player player = getPlayer();
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            tellError("Dieser Spieler konnte nicht gefunden werden!");
            return;
        }

        tellSuccess("Der Spieler '"+target.getName()+"' wurde erfolgreich gekickt!");
        target.kickPlayer(kickMessage());
    }

    @Override
    protected List<String> tabComplete() {

        if (args.length == 1) {
            return completeLastWordPlayerNames();
        }

        if (args.length == 2) {
            return List.of("benimm dich jetzt!");
        }

        return super.tabComplete();
    }

    public String kickMessage() {
        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        return "§c§lDu wurdest gekickt!\n\n" +
                "§7Grund: " + reason + "\n" +
                "§7Von: " + sender.getName() + "\n";
    }
}
