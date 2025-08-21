package net.vulcandev.vulcanapi.vulcanevents.events;

import net.vulcandev.vulcanevents.enums.EventType;
import net.vulcandev.vulcanapi.vulcanevents.types.EventTypeWrapper;
import net.vulcandev.vulcanevents.interfaces.IEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player leaves a VulcanEvent
 */
public class PlayerLeaveEventEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final Player player;
    private final EventTypeWrapper eventType;
    private final String eventName;
    private final boolean wasParticipant;
    private final boolean wasSpectator;
    private final boolean sendMessage;
    
    public PlayerLeaveEventEvent(@NotNull Player player, @NotNull IEvent event, boolean wasParticipant, boolean wasSpectator, boolean sendMessage) {
        this.player = player;
        this.eventType = EventTypeWrapper.fromVulcanEventType(event.getEventType());
        this.eventName = event.getName();
        this.wasParticipant = wasParticipant;
        this.wasSpectator = wasSpectator;
        this.sendMessage = sendMessage;
    }
    
    /**
     * Gets the player leaving the event
     * @return the Player
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    
    /**
     * Gets the type of event the player is leaving
     * @return the EventTypeWrapper
     */
    @NotNull
    public EventTypeWrapper getEventType() {
        return eventType;
    }
    
    /**
     * Gets the name of the event the player is leaving
     * @return the event name
     */
    @NotNull
    public String getEventName() {
        return eventName;
    }
    
    /**
     * Checks if the player was a participant (not spectator)
     * @return true if the player was participating
     */
    public boolean wasParticipant() {
        return wasParticipant;
    }
    
    /**
     * Checks if the player was a spectator
     * @return true if the player was spectating
     */
    public boolean wasSpectator() {
        return wasSpectator;
    }
    
    /**
     * Checks if a leave message will be sent to the player
     * @return true if a message will be sent
     */
    public boolean willSendMessage() {
        return sendMessage;
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
