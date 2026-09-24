package net.vulcandev.vulcanapi.raid.event;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.UUID;

@Getter
public final class RaidGuardDeniedEvent extends VulcanEvent {
    private final UUID nodeId;
    private final String action;
    private final String reason;

    @ApiStatus.Internal
    public RaidGuardDeniedEvent(UUID nodeId, String action, String reason) {
        this.nodeId = nodeId;
        this.action = action;
        this.reason = reason;
    }

    @Override
    public boolean isCancellable() { return false; }
}
