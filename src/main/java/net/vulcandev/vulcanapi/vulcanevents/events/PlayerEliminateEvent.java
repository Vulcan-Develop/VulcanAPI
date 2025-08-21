package net.vulcandev.vulcanapi.vulcanevents.events;

import net.vulcandev.vulcanapi.wrapper.EventTypeWrapper;
import net.vulcandev.vulcanevents.interfaces.IEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a player is eliminated from a VulcanEvent
 * This is fired when a player is removed from participants and moved to spectators
 */
public class PlayerEliminateEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final Player player;
    private final EventTypeWrapper eventType;
    private final String eventName;
    private final String reason;
    private final Player killer;
    
    public PlayerEliminateEvent(@NotNull Player player, @NotNull IEvent event, @Nullable String reason, @Nullable Player killer) {
        this.player = player;
        this.eventType = EventTypeWrapper.fromVulcanEventType(event.getEventType());
        this.eventName = event.getName();
        this.reason = reason;
        this.killer = killer;
    }
    
    /**
     * Gets the player who was eliminated
     * @return the Player
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    
    /**
     * Gets the type of event the player was eliminated from
     * @return the EventTypeWrapper
     */
    @NotNull
    public EventTypeWrapper getEventType() {
        return eventType;
    }
    
    /**
     * Gets the name of the event the player was eliminated from
     * @return the event name
     */
    @NotNull
    public String getEventName() {
        return eventName;
    }
    
    /**
     * Gets the reason for elimination
     * @return the elimination reason or null if not specified
     */
    @Nullable
    public String getReason() {
        return reason;
    }
    
    /**
     * Gets the player who eliminated this player (if applicable)
     * @return the killer Player or null if not killed by another player
     */
    @Nullable
    public Player getKiller() {
        return killer;
    }
    
    /**
     * Checks if the player was killed by another player
     * @return true if killed by another player, false otherwise
     */
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
