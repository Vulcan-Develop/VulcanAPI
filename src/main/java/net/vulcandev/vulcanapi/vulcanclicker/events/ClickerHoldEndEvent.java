package net.vulcandev.vulcanapi.vulcanclicker.events;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.vulcanclicker.ClickerType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired on the server thread when a hold or inventory refill ends, with the number of clicks the
 * clicker made during it.
 */
@Getter
public class ClickerHoldEndEvent extends VulcanEvent {
    private final Player player;
    private final ClickerType type;
    private final long clicks;
    private final long heldMillis;

    @ApiStatus.Internal
    public ClickerHoldEndEvent(Player player, ClickerType type, long clicks, long heldMillis) {
        this.player = player;
        this.type = type;
        this.clicks = clicks;
        this.heldMillis = heldMillis;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }
}
