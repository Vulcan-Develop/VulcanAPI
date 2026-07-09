package net.vulcandev.vulcanapi.vulcanevents.events;

import lombok.Getter;
import net.vulcandev.vulcanapi.wrapper.EventTypeWrapper;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class EventStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final EventTypeWrapper eventType;
    private final String eventName;

    public EventStartEvent(@NotNull EventTypeWrapper eventType, @NotNull String eventName) {
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
