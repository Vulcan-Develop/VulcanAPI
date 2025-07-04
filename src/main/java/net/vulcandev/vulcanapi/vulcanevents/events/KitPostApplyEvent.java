package net.vulcandev.vulcanapi.vulcanevents.events;

import net.vulcandev.vulcanevents.enums.EventType;
import net.vulcandev.vulcanevents.interfaces.IEvent;
import net.vulcandev.vulcanevents.interfaces.Kit;
import net.vulcandev.vulcanevents.objects.EventPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called after a kit is applied to a player in a VulcanEvent
 */
public class KitPostApplyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final EventPlayer eventPlayer;
    private final Kit kit;
    private final IEvent event;
    private final EventType eventType;
    private final String eventName;

    public KitPostApplyEvent(@NotNull Player player, @NotNull EventPlayer eventPlayer, @Nullable Kit kit, @NotNull IEvent event) {
        this.player = player;
        this.eventPlayer = eventPlayer;
        this.kit = kit;
        this.event = event;
        this.eventType = event.getEventType();
        this.eventName = event.getName();
    }
    
    /**
     * Gets the player receiving the kit
     * @return the Player
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Gets the EventPlayer wrapper for the player
     * @return the EventPlayer
     */
    @NotNull
    public EventPlayer getEventPlayer() {
        return eventPlayer;
    }
    
    /**
     * Gets the kit being applied
     * @return the Kit or null if no kit is configured
     */
    @Nullable
    public Kit getKit() {
        return kit;
    }
    
    /**
     * Gets the event where the kit is being applied
     * @return the IEvent instance
     */
    @NotNull
    public IEvent getEvent() {
        return event;
    }
    
    /**
     * Gets the type of event where the kit is being applied
     * @return the EventType
     */
    @NotNull
    public EventType getEventType() {
        return eventType;
    }
    
    /**
     * Gets the name of the event where the kit is being applied
     * @return the event name
     */
    @NotNull
    public String getEventName() {
        return eventName;
    }
    
    /**
     * Checks if a kit is actually configured for this event
     * @return true if a kit exists, false if null
     */
    public boolean hasKit() {
        return kit != null;
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
