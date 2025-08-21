package net.vulcandev.vulcanapi.vulcantools.events;

import net.vulcandev.vulcanapi.vulcantools.wrapper.ToolModeWrapper;
import net.vulcandev.vulcantools.enums.ToolMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Event fired when a player harvests wood using a VulcanTools lumber axe
 */
public class LumberHarvestEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final List<Block> harvestedBlocks;
    private final Material woodType;
    private final BlockBreakEvent originalEvent;
    private final ToolModeWrapper toolMode;
    private int amount;
    private boolean cancelled;
    
    /**
     * Creates a new LumberHarvestEvent.
     *
     * @param player the player who harvested the wood
     * @param harvestedBlocks the list of blocks that were harvested
     * @param woodType the type of wood that was harvested
     * @param originalEvent the original Bukkit BlockBreakEvent
     * @param toolMode the tool mode of the lumber axe used
     * @param amount the amount of wood harvested
     */
        public LumberHarvestEvent(Player player, List<Block> harvestedBlocks, Material woodType, BlockBreakEvent originalEvent, ToolMode toolMode, int amount) {
        this.player = player;
        this.harvestedBlocks = harvestedBlocks;
        this.woodType = woodType;
        this.originalEvent = originalEvent;
        this.toolMode = ToolModeWrapper.fromVulcanToolMode(toolMode);
        this.amount = amount;
        this.cancelled = false;
    }

    /**
     * Gets the player who harvested the wood.
     *
     * @return the player who harvested the wood
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets all blocks that were harvested in this event.
     *
     * @return the list of harvested blocks
     */
    public List<Block> getHarvestedBlocks() {
        return harvestedBlocks;
    }

    /**
     * Gets the type of wood that was harvested.
     *
     * @return the wood material type
     */
    public Material getWoodType() {
        return woodType;
    }

    /**
     * Gets the original Bukkit BlockBreakEvent that triggered this event.
     *
     * @return the original block break event
     */
    public BlockBreakEvent getOriginalEvent() {
        return originalEvent;
    }

    /**
     * Gets the tool mode of the lumber axe used.
     *
     * @return the tool mode of the lumber axe
     */
    public ToolModeWrapper getToolMode() {
        return toolMode;
    }

    /**
     * Gets the amount of wood harvested.
     *
     * @return the number of wood blocks harvested
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of wood harvested.
     *
     * @param amount the new number of wood blocks harvested
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Gets the amount of wood harvested (alias for getAmount).
     *
     * @return the number of wood blocks harvested
     */
    public int getHarvestedAmount() {
        return amount;
    }

    /**
     * Gets the original Bukkit BlockBreakEvent (alias for getOriginalEvent).
     *
     * @return the original block break event
     */
    public BlockBreakEvent getBukkitEvent() {
        return originalEvent;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        if (cancelled) {
            originalEvent.setCancelled(true);
        }
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
