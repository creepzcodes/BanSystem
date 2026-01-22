package org.creepz.util.database.ban;

import org.creepz.util.database.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BanManager {

    private final DatabaseConnection db;
    private final Map<UUID, Ban> cache = new HashMap<>();
    public BanManager(DatabaseConnection db) {
        this.db = db;
    }

    public void ban(UUID uuid, String reason, String bannedBy, long duration) {
        long start = System.currentTimeMillis();
        long end = duration == -1 ? -1 : start + duration;

        Ban ban = new Ban(uuid, reason, bannedBy, start, end);
        cache.put(uuid, ban);

        try {
            PreparedStatement statement = db.getConnection().prepareStatement(
                    " REPLACE INTO bans VALUES (?,?,?,?,?)"
            );
            statement.setString(1, uuid.toString());
            statement.setString(2, reason);
            statement.setString(3, bannedBy);
            statement.setLong(4, start);
            statement.setLong(5, end);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unBan(UUID uuid) {
        cache.remove(uuid);

        try {
            PreparedStatement statement = db.getConnection().prepareStatement(
                    "DELETE FROM bans WHERE uuid=?"
            );
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isBanned(UUID uuid) {
        if (!cache.containsKey(uuid)) return false;

        Ban ban = cache.get(uuid);
        if (ban.isExpired()) {
            unBan(uuid);
            return false;
        }
        return true;
    }

    public Ban getBan(UUID uuid) {
        return cache.get(uuid);
    }

    public String getBanMessage(UUID uuid) {
        Ban ban = getBan(uuid);

        if (ban == null) {
            return "Du bist gebannt.";
        }

        String duration;
        if (ban.getEndTime() == -1) {
            duration = "Permanent";
        } else {
            duration = formatTime(ban.getEndTime() - System.currentTimeMillis());
        }

        return "§c§lDu bist gebannt!\n\n" +
                "§7Grund: " + ban.getReason() + "\n" +
                "§7Von: " + ban.getBannedBy() + "\n" +
                "§7Dauer: " + duration + "\n\n" +
                "§7Ban-ID: §8" + ban.getUuid().toString().substring(0, 8);
    }

    public String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        hours %= 24;
        minutes %= 60;
        seconds %= 60;

        StringBuilder sb = new StringBuilder();

        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");

        return sb.toString().trim();
    }
}
