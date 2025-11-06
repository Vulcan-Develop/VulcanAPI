package net.vulcandev.vulcanapi.vulcanevents.events;

import net.vulcandev.vulcanapi.wrapper.EventStateWrapper;
import net.vulcandev.vulcanapi.wrapper.EventTypeWrapper;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a VulcanEvent changes state (WAITING → WARMUP → RUNNING → FINISHED)
 */
public class EventStateChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final EventTypeWrapper eventType;
    private final String eventName;
    private final EventStateWrapper previousState;
    private final EventStateWrapper newState;

    public EventStateChangeEvent(@NotNull EventTypeWrapper eventType, @NotNull String eventName, @NotNull EventStateWrapper previousState, @NotNull EventStateWrapper newState) {
        this.eventType = eventType;
        this.eventName = eventName;
        this.previousState = previousState;
        this.newState = newState;
    }
    
    
    /**
     * Gets the type of event that changed state
     * @return the EventTypeWrapper
     */
    @NotNull
    public EventTypeWrapper getEventType() {
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
     * @return the previous EventStateWrapper
     */
    @NotNull
    public EventStateWrapper getPreviousState() {
        return previousState;
    }
    
    /**
     * Gets the new state of the event
     * @return the new EventStateWrapper
     */
    @NotNull
    public EventStateWrapper getNewState() {
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
