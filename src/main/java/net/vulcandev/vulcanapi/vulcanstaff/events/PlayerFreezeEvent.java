package net.vulcandev.vulcanapi.vulcanstaff.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player is frozen or unfrozen by staff.
 * This event can be cancelled to prevent the freeze state change.
 */
public class PlayerFreezeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    
    private final Player target;
    private final Player staff;
    private final boolean freezing;
    private final FreezeReason reason;
    
    /**
     * Constructs a new PlayerFreezeEvent.
     * @param target The player being frozen/unfrozen
     * @param staff The staff member performing the action (can be null for console)
     * @param freezing True if freezing, false if unfreezing
     * @param reason The reason for the freeze action
     */
    public PlayerFreezeEvent(Player target, Player staff, boolean freezing, FreezeReason reason) {
        this.target = target;
        this.staff = staff;
        this.freezing = freezing;
        this.reason = reason;
    }
    
    /**
     * Gets the player being frozen/unfrozen.
     * @return The target player
     */
    public Player getTarget() {
        return target;
    }
    
    /**
     * Gets the staff member performing the freeze action.
     * @return The staff player, or null if performed by console
     */
    public Player getStaff() {
        return staff;
    }
    
    /**
     * Checks if the player is being frozen.
     * @return True if freezing, false if unfreezing
     */
    public boolean isFreezing() {
        return freezing;
    }
    
    /**
     * Gets the reason for the freeze action.
     * @return The freeze reason
     */
    public FreezeReason getReason() {
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
     * Enum representing different reasons for freeze actions.
     */
    public enum FreezeReason {
        COMMAND,        // Freeze command used
        STAFF_MODE,     // Freeze via staff mode item
        QUIT,           // Player quit while frozen
        API,            // Changed via API call
        TIMEOUT         // Freeze timeout
    }
}
