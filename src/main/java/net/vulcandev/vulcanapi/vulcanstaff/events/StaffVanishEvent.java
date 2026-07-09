package net.vulcandev.vulcanapi.vulcanstaff.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class StaffVanishEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @Setter
    private boolean cancelled = false;
    private final Player player;
    private final boolean vanishing;
    private final VanishReason reason;

    public StaffVanishEvent(Player player, boolean vanishing, VanishReason reason) {
        this.player = player;
        this.vanishing = vanishing;
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

    public enum VanishReason {
        COMMAND,
        STAFF_MODE,
        JOIN,
        QUIT,
        API,
        PLUGIN
    }
}
