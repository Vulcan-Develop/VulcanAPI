package net.vulcandev.vulcanapi.replay.data;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public final class ReplayMarkerRequest {
    private final UUID playerId;
    private final String clipId;
    private final long timestamp;
    private final String type;
    private final String label;
    private final Map<String, String> metadata;

    public ReplayMarkerRequest(UUID playerId, String clipId, long timestamp, String type,
                               String label, Map<String, String> metadata) {
        if (playerId == null && (clipId == null || clipId.isEmpty())) {
            throw new IllegalArgumentException("A playerId or clipId is required");
        }
        this.playerId = playerId;
        this.clipId = clipId;
        this.timestamp = timestamp;
        this.type = type == null ? "custom" : type;
        this.label = label == null ? "Marker" : label;
        this.metadata = Collections.unmodifiableMap(new LinkedHashMap<>(
                metadata == null ? Collections.emptyMap() : metadata));
    }

    public UUID getPlayerId() { return playerId; }
    public String getClipId() { return clipId; }
    public long getTimestamp() { return timestamp; }
    public String getType() { return type; }
    public String getLabel() { return label; }
    public Map<String, String> getMetadata() { return metadata; }
}
