package net.vulcandev.vulcanapi.vulcanevents.events;

import net.vulcandev.vulcanapi.wrapper.EventTypeWrapper;
import net.vulcandev.vulcanevents.events.interfaces.IEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player joins a VulcanEvent
 * This event is cancellable - if cancelled, the player will not join the event
 */
public class PlayerJoinEventEvent extends Event implements Cancellable {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final Player player;
    private final EventTypeWrapper eventType;
    private final String eventName;
    private boolean cancelled = false;
    
    public PlayerJoinEventEvent(@NotNull Player player, @NotNull IEvent event) {
        this.player = player;
        this.eventType = EventTypeWrapper.fromVulcanEventType(event.getEventType());
        this.eventName = event.getName();
    }
    
    /**
     * Gets the player joining the event
     * @return the Player
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    
    /**
     * Gets the type of event the player is joining
     * @return the EventTypeWrapper
     */
    @NotNull
    public EventTypeWrapper getEventType() {
        return eventType;
    }
    
    /**
     * Gets the name of the event the player is joining
     * @return the event name
     */
    @NotNull
    public String getEventName() {
        return eventName;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
