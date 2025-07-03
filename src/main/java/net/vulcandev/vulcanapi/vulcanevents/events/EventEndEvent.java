package net.vulcandev.vulcanapi.vulcanevents.events;

import net.vulcandev.vulcanevents.enums.EventType;
import net.vulcandev.vulcanevents.interfaces.IEvent;
import net.vulcandev.vulcanevents.objects.EventPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Called when a VulcanEvent ends
 */
public class EventEndEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final IEvent event;
    private final EventType eventType;
    private final String eventName;
    private final Map<UUID, EventPlayer> finalParticipants;
    private final Map<UUID, EventPlayer> finalSpectators;
    private final boolean wasSilent;
    private final boolean hadRewards;
    
    public EventEndEvent(@NotNull IEvent event, @NotNull Map<UUID, EventPlayer> finalParticipants,
                         @NotNull Map<UUID, EventPlayer> finalSpectators, boolean wasSilent, boolean hadRewards) {
        this.event = event;
        this.eventType = event.getEventType();
        this.eventName = event.getName();
        this.finalParticipants = new HashMap<>(finalParticipants);
        this.finalSpectators = new HashMap<>(finalSpectators);
        this.wasSilent = wasSilent;
        this.hadRewards = hadRewards;
    }
    
    /**
     * Gets the event that ended
     * @return the IEvent instance
     */
    @NotNull
    public IEvent getEvent() {
        return event;
    }
    
    /**
     * Gets the type of event that ended
     * @return the EventType
     */
    @NotNull
    public EventType getEventType() {
        return eventType;
    }
    
    /**
     * Gets the name of the event that ended
     * @return the event name
     */
    @NotNull
    public String getEventName() {
        return eventName;
    }
    
    /**
     * Gets the final participants when the event ended
     * @return Map of UUID to EventPlayer
     */
    @NotNull
    public Map<UUID, EventPlayer> getFinalParticipants() {
        return finalParticipants;
    }
    
    /**
     * Gets the final spectators when the event ended
     * @return Map of UUID to EventPlayer
     */
    @NotNull
    public Map<UUID, EventPlayer> getFinalSpectators() {
        return finalSpectators;
    }
    
    /**
     * Gets the winners of the event (remaining participants)
     * @return List of EventPlayer winners
     */
    @NotNull
    public List<EventPlayer> getWinners() {
        return new ArrayList<>(finalParticipants.values());
    }
    
    /**
     * Checks if the event ended silently
     * @return true if the event ended silently
     */
    public boolean wasSilent() {
        return wasSilent;
    }
    
    /**
     * Checks if rewards were given
     * @return true if rewards were given
     */
    public boolean hadRewards() {
        return hadRewards;
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
