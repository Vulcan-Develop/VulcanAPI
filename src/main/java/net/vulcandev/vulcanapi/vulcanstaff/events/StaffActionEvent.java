package net.vulcandev.vulcanapi.vulcanstaff.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a staff member performs a logged action.
 * This event is informational and cannot be cancelled.
 */
public class StaffActionEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    
    private final Player staff;
    private final ActionType actionType;
    private final String target;
    private final Location location;
    private final String details;
    
    /**
     * Constructs a new StaffActionEvent.
     * @param staff The staff member performing the action
     * @param actionType The type of action performed
     * @param target The target of the action (player name, block type, etc.)
     * @param location The location where the action occurred
     * @param details Additional details about the action
     */
    public StaffActionEvent(Player staff, ActionType actionType, String target, Location location, String details) {
        this.staff = staff;
        this.actionType = actionType;
        this.target = target;
        this.location = location;
        this.details = details;
    }
    
    /**
     * Gets the staff member who performed the action.
     * @return The staff player
     */
    public Player getStaff() {
        return staff;
    }
    
    /**
     * Gets the type of action performed.
     * @return The action type
     */
    public ActionType getActionType() {
        return actionType;
    }
    
    /**
     * Gets the target of the action.
     * @return The target (player name, block type, etc.)
     */
    public String getTarget() {
        return target;
    }
    
    /**
     * Gets the location where the action occurred.
     * @return The location
     */
    public Location getLocation() {
        return location;
    }
    
    /**
     * Gets additional details about the action.
     * @return The action details
     */
    public String getDetails() {
        return details;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    /**
     * Enum representing different types of staff actions.
     */
    public enum ActionType {
        BLOCK_BREAK,
        BLOCK_PLACE,
        BLOCK_INTERACT,
        ITEM_DROP,
        ITEM_PICKUP,
        CREATIVE_GRAB,
        COMMAND_EXECUTE,
        CHAT_MESSAGE,
        SPAWNER_CREATE,
        SPAWNER_ADD,
        SPAWNER_REMOVE
    }
}
