package net.vulcandev.vulcanapi.replay.data;

import java.util.UUID;

public final class ReplayBufferView {
    private final UUID playerId;
    private final long startedAt;
    private final long newestAt;
    private final long bytes;
    private final int segments;
    private final boolean degraded;

    public ReplayBufferView(UUID playerId, long startedAt, long newestAt, long bytes,
                            int segments, boolean degraded) {
        this.playerId = playerId;
        this.startedAt = startedAt;
        this.newestAt = newestAt;
        this.bytes = bytes;
        this.segments = segments;
        this.degraded = degraded;
    }

    public UUID getPlayerId() { return playerId; }
    public long getStartedAt() { return startedAt; }
    public long getNewestAt() { return newestAt; }
    public long getBytes() { return bytes; }
    public int getSegments() { return segments; }
    public boolean isDegraded() { return degraded; }
}
