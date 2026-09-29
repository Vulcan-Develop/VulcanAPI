package net.vulcandev.vulcanapi.vulcanclicker.events;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.vulcanclicker.ClickerType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired on the server thread when an armed player starts holding a button, or starts an
 * inventory refill, and the clicker is about to click for them. Cancelling suppresses this hold
 * only; the clicker stays armed. The clicks-per-second may be changed.
 */
@Getter
public class ClickerHoldStartEvent extends VulcanEvent implements Cancellable {
    private final Player player;
    private final ClickerType type;

    private double cps;

    @ApiStatus.Internal
    public ClickerHoldStartEvent(Player player, ClickerType type, double cps) {
        this.player = player;
        this.type = type;
        this.cps = cps;
    }

    public void setCps(double cps) {
        if (Double.isNaN(cps)) throw new IllegalArgumentException("cps must be a number");
        this.cps = cps;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
