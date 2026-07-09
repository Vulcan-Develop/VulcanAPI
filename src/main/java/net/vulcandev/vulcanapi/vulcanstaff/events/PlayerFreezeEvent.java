package net.vulcandev.vulcanapi.vulcanstaff.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerFreezeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @Setter
    private boolean cancelled = false;
    private final Player target;
    private final Player staff;
    private final boolean freezing;
    private final FreezeReason reason;

    public PlayerFreezeEvent(Player target, Player staff, boolean freezing, FreezeReason reason) {
        this.target = target;
        this.staff = staff;
        this.freezing = freezing;
        this.reason = reason;
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

    public enum FreezeReason {
        COMMAND,
        STAFF_MODE,
        QUIT,
        API,
        TIMEOUT
    }
}
