package net.vulcandev.vulcanapi.fortress.event.impl;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.fortress.player.PlayerProfile;

@Getter
public class AlertToggleEvent extends VulcanEvent {
    private final PlayerProfile player;
    private final boolean state;

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
