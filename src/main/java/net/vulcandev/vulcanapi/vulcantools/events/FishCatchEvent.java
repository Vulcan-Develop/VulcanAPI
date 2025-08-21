package net.vulcandev.vulcanapi.vulcantools.events;

import net.vulcandev.vulcanapi.wrapper.ToolModeWrapper;
import net.vulcandev.vulcantools.enums.ToolMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerFishEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a player catches fish using a VulcanTools fishing rod
 */
public class FishCatchEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final PlayerFishEvent originalEvent;
    private final ToolModeWrapper toolMode;
    private int fishAmount;
    private boolean cancelled;
    
    /**
     * Creates a new FishCatchEvent.
     *
     * @param player the player who caught the fish
     * @param originalEvent the original Bukkit PlayerFishEvent
     * @param toolMode the tool mode of the fishing rod used
     * @param fishAmount the number of fish caught
     */
    public FishCatchEvent(Player player, PlayerFishEvent originalEvent, ToolMode toolMode, int fishAmount) {
        this.player = player;
        this.originalEvent = originalEvent;
        this.toolMode = ToolModeWrapper.fromVulcanToolMode(toolMode);
        this.fishAmount = fishAmount;
        this.cancelled = false;
    }

    /**
     * Gets the player who caught the fish.
     *
     * @return the player who caught the fish
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the original Bukkit PlayerFishEvent that triggered this event.
     *
     * @return the original PlayerFishEvent
     */
    public PlayerFishEvent getOriginalEvent() {
        return originalEvent;
    }

    /**
     * Gets the tool mode of the fishing rod used.
     *
     * @return the tool mode of the fishing rod
     */
    public ToolModeWrapper getToolMode() {
        return toolMode;
    }

    /**
     * Gets the amount of fish caught in this event.
     *
     * @return the number of fish caught
     */
    public int getFishAmount() {
        return fishAmount;
    }

    /**
     * Sets the amount of fish caught in this event.
     *
     * @param fishAmount the new number of fish caught
     */
    public void setFishAmount(int fishAmount) {
        this.fishAmount = fishAmount;
    }

    /**
     * Gets the amount of fish caught (alias for getFishAmount).
     *
     * @return the number of fish caught
     */
    public int getCaughtAmount() {
        return fishAmount;
    }

    /**
     * Gets the original Bukkit PlayerFishEvent (alias for getOriginalEvent).
     *
     * @return the original fishing event
     */
    public PlayerFishEvent getBukkitEvent() {
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
