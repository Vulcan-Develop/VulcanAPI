package net.vulcandev.vulcanapi.replay.data;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Structured evidence recorded alongside a replay, such as the geometry an anticheat check
 * decided on. The payload is an opaque JSON object whose shape is named by {@link #getSchema()};
 * the recorder stores it on its own track and never interprets it.
 *
 * <p>The record lands in the subject's rolling buffer, in the buffer of every player in
 * {@link #getAlsoRecordFor()}, and, when a world is given, in the buffers of players close enough
 * to the anchor to have seen it.
 */
public final class ReplayEvidenceRequest {
    private final UUID playerId;
    private final UUID evidenceId;
    private final long timestamp;
    private final String schema;
    private final String json;
    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final Set<UUID> alsoRecordFor;

    private ReplayEvidenceRequest(Builder builder) {
        this.playerId = builder.playerId;
        this.evidenceId = builder.evidenceId;
        this.timestamp = builder.timestamp;
        this.schema = builder.schema;
        this.json = builder.json;
        this.world = builder.world;
        this.x = builder.x;
        this.y = builder.y;
        this.z = builder.z;
        this.alsoRecordFor = Collections.unmodifiableSet(new LinkedHashSet<>(builder.alsoRecordFor));
    }

    public UUID getPlayerId() { return playerId; }
    /** Identifies this evidence; a marker for the same moment can carry it to link the two. */
    public UUID getEvidenceId() { return evidenceId; }
    /** Epoch millis of the behaviour the evidence describes. */
    public long getTimestamp() { return timestamp; }
    public String getSchema() { return schema; }
    /** The evidence itself: one JSON object. */
    public String getJson() { return json; }
    /** World of the fan-out anchor, or null to record only for the subject and named players. */
    public String getWorld() { return world; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public boolean hasAnchor() { return world != null; }
    public Set<UUID> getAlsoRecordFor() { return alsoRecordFor; }

    public static Builder builder(UUID playerId, String schema, String json) {
        return new Builder(playerId, schema, json);
    }

    public static final class Builder {
        private final UUID playerId;
        private final String schema;
        private final String json;
        private UUID evidenceId = UUID.randomUUID();
        private long timestamp = System.currentTimeMillis();
        private String world;
        private double x;
        private double y;
        private double z;
        private final Set<UUID> alsoRecordFor = new LinkedHashSet<>();

        private Builder(UUID playerId, String schema, String json) {
            if (playerId == null) throw new IllegalArgumentException("playerId");
            if (schema == null || schema.isEmpty()) throw new IllegalArgumentException("schema");
            if (json == null || json.isEmpty()) throw new IllegalArgumentException("json");
            this.playerId = playerId;
            this.schema = schema;
            this.json = json;
        }

        public Builder evidenceId(UUID value) { if (value != null) this.evidenceId = value; return this; }
        public Builder timestamp(long epochMillis) { if (epochMillis > 0L) this.timestamp = epochMillis; return this; }
        public Builder anchor(String world, double x, double y, double z) {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }
        public Builder alsoRecordFor(UUID playerId) {
            if (playerId != null && !playerId.equals(this.playerId)) this.alsoRecordFor.add(playerId);
            return this;
        }
        public ReplayEvidenceRequest build() { return new ReplayEvidenceRequest(this); }
    }
}
