package net.vulcandev.vulcanapi.vulcanevents.events;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.wrapper.EventTypeWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerJoinEventEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final EventTypeWrapper eventType;
    private final String eventName;

    @Setter
    private boolean cancelled = false;

    public PlayerJoinEventEvent(@NotNull Player player, @NotNull EventTypeWrapper eventType, @NotNull String eventName) {
        this.player = player;
        this.eventType = eventType;
        this.eventName = eventName;
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
