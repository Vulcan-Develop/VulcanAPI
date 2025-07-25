package net.vulcandev.vulcanapi.vulcantools.events;

import net.vulcandev.vulcantools.enums.ToolMode;
import net.vulcandev.vulcantools.enums.ToolType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a player changes the mode of a VulcanTools tool
 */
public class ToolModeChangeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final ItemStack tool;
    private final ToolType toolType;
    private final ToolMode oldMode;
    private ToolMode newMode;
    private boolean cancelled;
    
    /**
     * Creates a new ToolModeChangeEvent.
     *
     * @param player the player changing the tool mode
     * @param tool the tool whose mode is being changed
     * @param toolType the type of tool
     * @param oldMode the previous mode of the tool
     * @param newMode the new mode of the tool
     */
    public ToolModeChangeEvent(Player player, ItemStack tool, ToolType toolType, ToolMode oldMode, ToolMode newMode) {
        this.player = player;
        this.tool = tool;
        this.toolType = toolType;
        this.oldMode = oldMode;
        this.newMode = newMode;
        this.cancelled = false;
    }

    /**
     * Gets the player changing the tool mode.
     *
     * @return the player changing the tool mode
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the tool whose mode is being changed.
     *
     * @return the tool ItemStack
     */
    public ItemStack getTool() {
        return tool;
    }

    /**
     * Gets the type of tool whose mode is being changed.
     *
     * @return the tool type
     */
    public ToolType getToolType() {
        return toolType;
    }

    /**
     * Gets the previous mode of the tool.
     *
     * @return the old tool mode
     */
    public ToolMode getOldMode() {
        return oldMode;
    }

    /**
     * Gets the new mode of the tool.
     *
     * @return the new tool mode
     */
    public ToolMode getNewMode() {
        return newMode;
    }

    /**
     * Sets the new mode of the tool.
     *
     * @param newMode the new tool mode
     */
    public void setNewMode(ToolMode newMode) {
        this.newMode = newMode;
    }

    /**
     * Checks if the mode is actually changing.
     *
     * @return true if the mode is different, false if it's the same
     */
    public boolean isModeChanging() {
        return oldMode != newMode;
    }

    /**
     * Gets a description of the mode change.
     *
     * @return a string describing the change
     */
    public String getModeChangeDescription() {
        return oldMode.niceName() + " -> " + newMode.niceName();
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
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
