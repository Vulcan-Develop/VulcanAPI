package net.vulcandev.vulcanapi.vulcanstaff.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a staff member enters or exits staff mode.
 * This event can be cancelled to prevent the staff mode change.
 */
public class StaffModeToggleEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    
    private final Player player;
    private final boolean enteringStaffMode;
    
    /**
     * Constructs a new StaffModeToggleEvent.
     * @param player The player entering/exiting staff mode
     * @param enteringStaffMode True if entering staff mode, false if exiting
     */
    public StaffModeToggleEvent(Player player, boolean enteringStaffMode) {
        this.player = player;
        this.enteringStaffMode = enteringStaffMode;
    }
    
    /**
     * Gets the player involved in the staff mode event.
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Checks if the player is entering staff mode.
     * @return True if entering staff mode, false if exiting
     */
    public boolean isEnteringStaffMode() {
        return enteringStaffMode;
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
}
