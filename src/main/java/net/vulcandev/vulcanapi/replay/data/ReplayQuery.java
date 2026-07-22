package net.vulcandev.vulcanapi.replay.data;

import net.vulcandev.vulcanapi.replay.ReplayClipStatus;

import java.util.UUID;

public final class ReplayQuery {
    private final UUID playerId;
    private final ReplayClipStatus status;
    private final long from;
    private final long until;
    private final int offset;
    private final int limit;

    public ReplayQuery(UUID playerId, ReplayClipStatus status, long from, long until,
                       int offset, int limit) {
        this.playerId = playerId;
        this.status = status;
        this.from = from;
        this.until = until;
        this.offset = Math.max(0, offset);
        this.limit = Math.max(1, Math.min(100, limit));
    }

    public UUID getPlayerId() { return playerId; }
    public ReplayClipStatus getStatus() { return status; }
    public long getFrom() { return from; }
    public long getUntil() { return until; }
    public int getOffset() { return offset; }
    public int getLimit() { return limit; }
}
