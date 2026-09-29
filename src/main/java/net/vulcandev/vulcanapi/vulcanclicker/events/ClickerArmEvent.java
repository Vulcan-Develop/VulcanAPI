package net.vulcandev.vulcanapi.vulcanclicker.events;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.vulcanclicker.ClickerType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired before a clicker is armed or disarmed by a command or the API. Disarming on quit or
 * shutdown does not fire this event.
 */
@Getter
public class ClickerArmEvent extends VulcanEvent implements Cancellable {
    private final Player player;
    private final ClickerType type;
    private final boolean armed;
    private final Cause cause;

    @ApiStatus.Internal
    public ClickerArmEvent(Player player, ClickerType type, boolean armed, Cause cause) {
        this.player = player;
        this.type = type;
        this.armed = armed;
        this.cause = cause;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }

    public enum Cause {
        COMMAND,
        API
    }
}
