package net.vulcandev.vulcanapi.vulcanevents.events;

import net.vulcandev.vulcanevents.enums.EventType;
import net.vulcandev.vulcanevents.interfaces.IEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a VulcanEvent starts (transitions to WAITING state)
 */
public class EventStartEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final IEvent event;
    private final EventType eventType;
    private final String eventName;
    
    public EventStartEvent(@NotNull IEvent event) {
        this.event = event;
        this.eventType = event.getEventType();
        this.eventName = event.getName();
    }
    
    /**
     * Gets the event that started
     * @return the IEvent instance
     */
    @NotNull
    public IEvent getEvent() {
        return event;
    }
    
    /**
     * Gets the type of event that started
     * @return the EventType
     */
    @NotNull
    public EventType getEventType() {
        return eventType;
    }
    
    /**
     * Gets the name of the event that started
     * @return the event name
     */
    @NotNull
    public String getEventName() {
        return eventName;
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
