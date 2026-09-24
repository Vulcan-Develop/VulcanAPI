package net.vulcandev.vulcanapi.vulcanstaff;

import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

import java.util.UUID;

@Getter
public final class InventorySnapshot {
    private final UUID id;
    private final UUID playerId;
    private final long timestamp;
    private final String reason;

    @ApiStatus.Internal
    public InventorySnapshot(UUID id, UUID playerId, long timestamp, String reason) {
        this.id = id;
        this.playerId = playerId;
        this.timestamp = timestamp;
        this.reason = reason;
    }
}
