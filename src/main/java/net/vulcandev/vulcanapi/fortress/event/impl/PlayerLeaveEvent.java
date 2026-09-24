package net.vulcandev.vulcanapi.fortress.event.impl;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.UUID;

@Getter
public class PlayerLeaveEvent extends VulcanEvent {
    private final UUID playerUuid;

    @ApiStatus.Internal
    public PlayerLeaveEvent(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }
}
