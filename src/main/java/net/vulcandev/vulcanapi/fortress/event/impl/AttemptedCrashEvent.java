package net.vulcandev.vulcanapi.fortress.event.impl;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.fortress.player.PlayerProfile;
import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus;

@Getter
public class AttemptedCrashEvent extends VulcanEvent implements Cancellable {
    private final PlayerProfile player;
    private final String reason;
    private final Location location;

    @Setter
    private boolean cancelled = false;

    @Setter
    private boolean autoKick = true;

    @ApiStatus.Internal
    public AttemptedCrashEvent(PlayerProfile player, String reason, Location location) {
        this.player = player;
        this.reason = reason;
        this.location = location.clone();
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
