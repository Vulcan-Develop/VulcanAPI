package net.vulcandev.vulcanapi.fortress.event.impl;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.fortress.player.PlayerProfile;
import org.jetbrains.annotations.ApiStatus;

@Getter
public class AlertToggleEvent extends VulcanEvent {
    private final PlayerProfile player;
    private final boolean state;

    @ApiStatus.Internal
    public AlertToggleEvent(PlayerProfile player, boolean state) {
        this.player = player;
        this.state = state;
    }

    public boolean isAlertsEnabled() {
        return state;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }
}
