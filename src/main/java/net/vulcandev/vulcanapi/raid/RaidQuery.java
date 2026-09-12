package net.vulcandev.vulcanapi.raid;

import lombok.Getter;

import java.util.UUID;

@Getter
public final class RaidQuery {
    private final String factionId;
    private final RaidStatus status;
    private final UUID rootId;
    private final int limit;

    private RaidQuery(Builder builder) {
        this.factionId = builder.factionId;
        this.status = builder.status;
        this.rootId = builder.rootId;
        this.limit = builder.limit;
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String factionId;
        private RaidStatus status;
        private UUID rootId;
        private int limit = 100;

        public Builder factionId(String factionId) {
            this.factionId = factionId;
            return this;
        }

        public Builder status(RaidStatus status) {
            this.status = status;
            return this;
        }

        public Builder rootId(UUID rootId) {
            this.rootId = rootId;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = Math.max(1, Math.min(limit, 1000));
            return this;
        }

        public RaidQuery build() {
            return new RaidQuery(this);
        }
    }
}
