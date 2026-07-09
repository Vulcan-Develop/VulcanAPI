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
public class PlayerSpectateEventEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final EventTypeWrapper eventType;
    private final String eventName;
    private final boolean wasParticipant;

    @Setter
    private boolean cancelled = false;

    public PlayerSpectateEventEvent(@NotNull Player player, @NotNull EventTypeWrapper eventType, @NotNull String eventName, boolean wasParticipant) {
        this.player = player;
        this.eventType = eventType;
        this.eventName = eventName;
        this.wasParticipant = wasParticipant;
    }

    public boolean wasParticipant() {
        return wasParticipant;
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
