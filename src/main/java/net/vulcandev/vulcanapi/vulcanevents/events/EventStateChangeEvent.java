package net.vulcandev.vulcanapi.vulcanevents.events;

import net.vulcandev.vulcanevents.enums.EventState;
import net.vulcandev.vulcanevents.enums.EventType;
import net.vulcandev.vulcanevents.interfaces.IEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a VulcanEvent changes state (WAITING → WARMUP → RUNNING → FINISHED)
 */
public class EventStateChangeEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final IEvent event;
    private final EventType eventType;
    private final String eventName;
    private final EventState previousState;
    private final EventState newState;
    
    public EventStateChangeEvent(@NotNull IEvent event, @NotNull EventState previousState, @NotNull EventState newState) {
        this.event = event;
        this.eventType = event.getEventType();
        this.eventName = event.getName();
        this.previousState = previousState;
        this.newState = newState;
    }
    
    /**
     * Gets the event that changed state
     * @return the IEvent instance
     */
    @NotNull
    public IEvent getEvent() {
        return event;
    }
    
    /**
     * Gets the type of event that changed state
     * @return the EventType
     */
    @NotNull
    public EventType getEventType() {
        return eventType;
    }
    
    /**
     * Gets the name of the event that changed state
     * @return the event name
     */
    @NotNull
    public String getEventName() {
        return eventName;
    }
    
    /**
     * Gets the previous state of the event
     * @return the previous EventState
     */
    @NotNull
    public EventState getPreviousState() {
        return previousState;
    }
    
    /**
     * Gets the new state of the event
     * @return the new EventState
     */
    @NotNull
    public EventState getNewState() {
        return newState;
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
