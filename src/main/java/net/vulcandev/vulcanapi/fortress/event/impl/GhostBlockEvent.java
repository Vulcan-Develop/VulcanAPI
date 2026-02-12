package net.vulcandev.vulcanapi.fortress.event.impl;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.fortress.player.PlayerProfile;
import org.bukkit.Location;

@Getter
public class GhostBlockEvent extends VulcanEvent implements Cancellable {
    private final PlayerProfile player;
    private final String reason;
    private final Location playerLocation;
    private final Location blockLocation;

    @Setter
    private boolean cancelled = false;

    @Setter
    private boolean mitigated;

    @Setter
    private boolean suppressCorrection = false; // Allow plugins to handle correction themselves

    public GhostBlockEvent(PlayerProfile player, String reason, Location playerLocation,
                           Location blockLocation, boolean mitigated) {
        this.player = player;
        this.reason = reason;
        this.playerLocation = playerLocation.clone();
        this.blockLocation = blockLocation.clone();
        this.mitigated = mitigated;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}