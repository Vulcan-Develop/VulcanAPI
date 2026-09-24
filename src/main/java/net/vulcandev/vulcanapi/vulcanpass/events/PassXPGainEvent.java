package net.vulcandev.vulcanapi.vulcanpass.events;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired before pass XP is applied. The amount has already been multiplied by the premium
 * bonus where applicable, and may be adjusted by a listener.
 */
@Getter
public class PassXPGainEvent extends VulcanEvent implements Cancellable {
    private final Player player;
    private final String source;

    @Setter
    private double amount;

    @ApiStatus.Internal
    public PassXPGainEvent(Player player, double amount, String source) {
        this.player = player;
        this.amount = amount;
        this.source = source;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
