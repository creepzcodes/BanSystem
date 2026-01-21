package org.creepz.util.database;

import lombok.Getter;

import java.util.UUID;

public class Ban {
    @Getter
    private final UUID uuid;
    @Getter
    private final String reason;
    @Getter
    private final String bannedBy;
    @Getter
    private final long startTime;
    @Getter
    private final long endTime;

    public Ban(UUID uuid, String reason, String bannedBy, long startTime, long endTime) {
        this.uuid = uuid;
        this.reason = reason;
        this.bannedBy = bannedBy;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public boolean isPermanent() {
        return endTime == -1;
    }

    public boolean isExpired() {
        return !isPermanent() && System.currentTimeMillis() > endTime;
    }
}
