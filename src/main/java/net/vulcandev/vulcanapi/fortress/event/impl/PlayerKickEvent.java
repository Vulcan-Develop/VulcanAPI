package net.vulcandev.vulcanapi.fortress.event.impl;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.fortress.player.PlayerProfile;

@Getter
public class PlayerKickEvent extends VulcanEvent implements Cancellable {
    private final PlayerProfile player;
    private final String reason;
    @Setter
    private boolean cancelled = false;

    public PlayerKickEvent(PlayerProfile player, String reason) {
        this.player = player;
        this.reason = reason;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
