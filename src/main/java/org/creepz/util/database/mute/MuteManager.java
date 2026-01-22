package org.creepz.util.database.mute;

import org.creepz.util.database.DatabaseConnection;
import org.creepz.util.database.ban.Ban;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MuteManager {

    private final DatabaseConnection db;
    private final Map<UUID, Mute> cache = new HashMap<>();

    public MuteManager(DatabaseConnection db) {
        this.db = db;
    }

    public void mute(UUID uuid, String reason, String mutedBy, long duration) {
        long start = System.currentTimeMillis();
        long end = duration == -1 ? -1 : start + duration;

        Mute mute = new Mute(uuid, reason, mutedBy, start, end);
        cache.put(uuid, mute);

        try {
            PreparedStatement statement = db.getConnection().prepareStatement(
                    "REPLACE INTO mutes VALUES (?,?,?,?,?)"
            );
            statement.setString(1, uuid.toString());
            statement.setString(2, reason);
            statement.setString(3, mutedBy);
            statement.setString(4, String.valueOf(start));
            statement.setString(5, String.valueOf(end));
            statement.executeUpdate();
        } catch (SQLException e)  {
            e.printStackTrace();
        }
    }

    public void unmute(UUID uuid) {
        cache.remove(uuid);

        try {
            PreparedStatement statement = db.getConnection().prepareStatement(
                    "DELETE FROM mutes WHERE uuid=?"
            );
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        }  catch (SQLException e)  {
            e.printStackTrace();
        }
    }

    public boolean isMuted(UUID uuid) {
        if (!cache.containsKey(uuid)) return false;

        Mute mute = cache.get(uuid);
        if (mute.isExpired()) {
            cache.remove(uuid);
            return false;
        }
        return true;
    }

    public Mute getMute(UUID uuid) {
        return cache.get(uuid);
    }

    public String getMuteMessage(UUID uuid) {
        Mute mute = getMute(uuid);

        if (mute == null) {
            return "Du bist gebannt.";
        }

        String duration;
        if (mute.getEndTime() == -1) {
            duration = "Permanent";
        } else {
            duration = formatTime(mute.getEndTime() - System.currentTimeMillis());
        }

        return "§c§lDu wurdest gemuted!\n\n" +
                "§7Grund: " + mute.getReason() + "\n" +
                "§7Von: " + mute.getMutedBy() + "\n" +
                "§7Dauer: " + duration + "\n\n" +
                "§7Mute-ID: §8" + mute.getUuid().toString().substring(0, 8);
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
