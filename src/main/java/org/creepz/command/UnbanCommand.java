package org.creepz.command;

import org.creepz.util.database.BanManager;
import org.mineacademy.fo.command.SimpleCommand;

public class UnbanCommand extends SimpleCommand {

    private final BanManager banManager;

    public UnbanCommand(BanManager banManager) {
        super("unban");
        this.banManager = banManager;
    }

    @Override
    protected void onCommand() {

    }
}
