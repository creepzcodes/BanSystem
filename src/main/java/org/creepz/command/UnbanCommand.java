package org.creepz.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.creepz.util.database.ban.BanManager;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

public class UnbanCommand extends SimpleCommand {

    private final BanManager banManager;

    public UnbanCommand(BanManager banManager) {
        super("unban");
        this.banManager = banManager;
    }

    @Override
    protected void onCommand() {

        //checkArgs(0, "Usage: unban <player>");
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (!banManager.isBanned(target.getUniqueId())) {
            tellError("Der Spieler ist nicht gebannt!");
            return;
        }

        banManager.unBan(target.getUniqueId());
        tellSuccess("Der Spieler '"+target.getName()+"' wurde entbannt!");
    }

    @Override
    protected List<String> tabComplete() {

        if (args.length == 0) {
            return completeLastWordPlayerNames();
        }

        return super.tabComplete();
    }
}
