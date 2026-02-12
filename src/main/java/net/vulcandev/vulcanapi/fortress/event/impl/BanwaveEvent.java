package net.vulcandev.vulcanapi.fortress.event.impl;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;

import java.util.Set;
import java.util.UUID;

@Getter
public class BanwaveEvent extends VulcanEvent implements Cancellable {

    private final Set<UUID> affectedPlayers;

    public BanwaveEvent(Set<UUID> affectedPlayers) {
        this.affectedPlayers = affectedPlayers;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
