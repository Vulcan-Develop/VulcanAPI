package net.vulcandev.vulcanapi.replay.data;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public final class ReplaySaveRequest {
    private final UUID playerId;
    private final int lookbackSeconds;
    private final String source;
    private final String reason;
    private final UUID actorId;
    private final boolean publish;
    private final Map<String, String> metadata;

    private ReplaySaveRequest(Builder builder) {
        this.playerId = builder.playerId;
        this.lookbackSeconds = builder.lookbackSeconds;
        this.source = builder.source;
        this.reason = builder.reason;
        this.actorId = builder.actorId;
        this.publish = builder.publish;
        this.metadata = Collections.unmodifiableMap(new LinkedHashMap<>(builder.metadata));
    }

    public UUID getPlayerId() { return playerId; }
    public int getLookbackSeconds() { return lookbackSeconds; }
    public String getSource() { return source; }
    public String getReason() { return reason; }
    public UUID getActorId() { return actorId; }
    public boolean isPublish() { return publish; }
    public Map<String, String> getMetadata() { return metadata; }

    public static Builder builder(UUID playerId) { return new Builder(playerId); }

    public static final class Builder {
        private final UUID playerId;
        private int lookbackSeconds = 300;
        private String source = "api";
        private String reason = "API request";
        private UUID actorId;
        private boolean publish = true;
        private final Map<String, String> metadata = new LinkedHashMap<>();

        private Builder(UUID playerId) {
            if (playerId == null) throw new IllegalArgumentException("playerId");
            this.playerId = playerId;
        }

        public Builder lookbackSeconds(int value) { this.lookbackSeconds = value; return this; }
        public Builder source(String value) { this.source = value; return this; }
        public Builder reason(String value) { this.reason = value; return this; }
        public Builder actorId(UUID value) { this.actorId = value; return this; }
        public Builder publish(boolean value) { this.publish = value; return this; }
        public Builder metadata(String key, String value) { this.metadata.put(key, value); return this; }
        public ReplaySaveRequest build() { return new ReplaySaveRequest(this); }
    }
}
