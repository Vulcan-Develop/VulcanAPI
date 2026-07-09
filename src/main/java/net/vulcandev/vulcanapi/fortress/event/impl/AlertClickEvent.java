package net.vulcandev.vulcanapi.fortress.event.impl;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import org.bukkit.entity.Player;

/**
 * Fired when a staff member clicks a Fortress violation alert to teleport to the
 * flagged player. Cancel to suppress Fortress's default teleport.
 */
@Getter
public class AlertClickEvent extends VulcanEvent implements Cancellable {
    private final Player staff;
    private final Player target;
    private final String targetName;

    @Setter
    private boolean cancelled = false;

    public AlertClickEvent(Player staff, Player target, String targetName) {
        this.staff = staff;
        this.target = target;
        this.targetName = targetName;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
