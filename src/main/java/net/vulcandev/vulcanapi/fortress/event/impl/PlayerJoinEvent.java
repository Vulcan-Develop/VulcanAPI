package net.vulcandev.vulcanapi.fortress.event.impl;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.fortress.player.PlayerProfile;

@Getter
public class PlayerJoinEvent extends VulcanEvent {
    private final PlayerProfile player;
    private final int protocolVersion;

    public PlayerJoinEvent(PlayerProfile player, int protocolVersion) {
        this.player = player;
        this.protocolVersion = protocolVersion;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }
}