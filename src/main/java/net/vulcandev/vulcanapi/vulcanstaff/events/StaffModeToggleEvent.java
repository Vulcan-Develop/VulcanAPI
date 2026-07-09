package net.vulcandev.vulcanapi.vulcanstaff.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class StaffModeToggleEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @Setter
    private boolean cancelled = false;
    private final Player player;
    private final boolean enteringStaffMode;

    public StaffModeToggleEvent(Player player, boolean enteringStaffMode) {
        this.player = player;
        this.enteringStaffMode = enteringStaffMode;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
