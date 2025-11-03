package net.vulcandev.vulcanapi.vulcanevents.events;

import net.vulcandev.vulcanapi.wrapper.EventTypeWrapper;
import net.vulcandev.vulcanevents.events.interfaces.IEvent;
import net.vulcandev.vulcanevents.objects.EventPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Called when a VulcanEvent ends
 */
public class EventEndEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final EventTypeWrapper eventType;
    private final String eventName;
    private final Map<UUID, Player> finalParticipants;
    private final Map<UUID, Player> finalSpectators;
    private final boolean wasSilent;
    private final boolean hadRewards;
    
    public EventEndEvent(@NotNull IEvent event, @NotNull Map<UUID, EventPlayer> finalParticipants, @NotNull Map<UUID, EventPlayer> finalSpectators, boolean wasSilent, boolean hadRewards) {
        this.eventType = EventTypeWrapper.fromVulcanEventType(event.getEventType());
        this.eventName = event.getName();

        // Convert from EventPlayer → Player safely
        this.finalParticipants = new HashMap<>();
        for (Map.Entry<UUID, EventPlayer> entry : finalParticipants.entrySet()) {
            Player player = entry.getValue().getPlayer();
            if (player != null) {
                this.finalParticipants.put(entry.getKey(), player);
            }
        }

        this.finalSpectators = new HashMap<>();
        for (Map.Entry<UUID, EventPlayer> entry : finalSpectators.entrySet()) {
            Player player = entry.getValue().getPlayer();
            if (player != null) {
                this.finalSpectators.put(entry.getKey(), player);
            }
        }

        this.wasSilent = wasSilent;
        this.hadRewards = hadRewards;
    }
    
    
    /**
     * Gets the type of event that ended
     * @return the EventTypeWrapper
     */
    @NotNull
    public EventTypeWrapper getEventType() {
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
     * @return Map of UUID to Player
     */
    @NotNull
    public Map<UUID, Player> getFinalParticipants() {
        return finalParticipants;
    }
    
    /**
     * Gets the final spectators when the event ended
     * @return Map of UUID to Player
     */
    @NotNull
    public Map<UUID, Player> getFinalSpectators() {
        return finalSpectators;
    }
    
    /**
     * Gets the winners of the event (remaining participants)
     * @return List of Player winners
     */
    @NotNull
    public List<Player> getWinners() {
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
