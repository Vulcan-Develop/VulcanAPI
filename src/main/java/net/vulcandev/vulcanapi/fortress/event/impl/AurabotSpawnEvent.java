package net.vulcandev.vulcanapi.fortress.event.impl;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.fortress.player.PlayerProfile;
import org.bukkit.Location;

@Getter
public class AurabotSpawnEvent extends VulcanEvent implements Cancellable {
    private final PlayerProfile player;
    private final int botId;
    private final Location location;

    @Setter
    private boolean cancelled = false;

    public AurabotSpawnEvent(PlayerProfile player, int id, Location location) {
        this.player = player;
        this.botId = id;
        this.location = location.clone();
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
