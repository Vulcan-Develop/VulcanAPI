package net.vulcandev.vulcanapi.vulcanstaff.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a staff member enters or exits vanish mode.
 * This event can be cancelled to prevent the vanish state change.
 */
public class StaffVanishEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    
    private final Player player;
    private final boolean vanishing;
    private final VanishReason reason;
    
    /**
     * Constructs a new StaffVanishEvent.
     * @param player The player entering/exiting vanish
     * @param vanishing True if entering vanish, false if exiting
     * @param reason The reason for the vanish state change
     */
    public StaffVanishEvent(Player player, boolean vanishing, VanishReason reason) {
        this.player = player;
        this.vanishing = vanishing;
        this.reason = reason;
    }
    
    /**
     * Gets the player involved in the vanish event.
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Checks if the player is entering vanish mode.
     * @return True if entering vanish, false if exiting
     */
    public boolean isVanishing() {
        return vanishing;
    }
    
    /**
     * Gets the reason for the vanish state change.
     * @return The vanish reason
     */
    public VanishReason getReason() {
        return reason;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    /**
     * Enum representing different reasons for vanish state changes.
     */
    public enum VanishReason {
        COMMAND,        // Player used vanish command
        STAFF_MODE,     // Vanish toggled via staff mode
        JOIN,           // Auto-vanish on join
        QUIT,           // Vanish state on quit
        API,            // Changed via API call
        PLUGIN          // Changed by another plugin
    }
}
