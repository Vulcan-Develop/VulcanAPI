package net.vulcandev.vulcanapi.fortress.event.impl;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.fortress.player.PlayerProfile;

/**
 * Event fired when a player toggles their alerts
 */

@Getter
public class AlertToggleEvent extends VulcanEvent {
    private final PlayerProfile player;
    private final boolean state; //true = alerts off -> on, false = alerts on -> off, this does NOT handle the on-join alert enabling

    public AlertToggleEvent(PlayerProfile player, boolean state) {
        this.player = player;
        this.state = state;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }
}
