package net.vulcandev.vulcanapi.vulcanevents.events;

import lombok.Getter;
import net.vulcandev.vulcanapi.wrapper.EventTypeWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class PlayerEliminateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final EventTypeWrapper eventType;
    private final String eventName;
    private final String reason;
    private final Player killer;

    public PlayerEliminateEvent(@NotNull Player player, @NotNull EventTypeWrapper eventType, @NotNull String eventName, @Nullable String reason, @Nullable Player killer) {
        this.player = player;
        this.eventType = eventType;
        this.eventName = eventName;
        this.reason = reason;
        this.killer = killer;
    }

    public boolean wasKilledByPlayer() {
        return killer != null;
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
