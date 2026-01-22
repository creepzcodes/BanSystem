package org.creepz.util.database.mute;

import lombok.Getter;

import java.util.UUID;

public class Mute {
    @Getter
    private final UUID uuid;
    @Getter
    private final String reason;
    @Getter
    private final String mutedBy;
    @Getter
    private final long startTime;
    @Getter
    private final long endTime;

    public Mute(UUID uuid, String reason, String mutedBy, long startTime, long endTime) {
        this.uuid = uuid;
        this.reason = reason;
        this.mutedBy = mutedBy;
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
