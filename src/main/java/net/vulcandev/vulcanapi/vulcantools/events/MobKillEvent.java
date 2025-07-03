package net.vulcandev.vulcanapi.vulcantools.events;

import net.vulcandev.vulcantools.enums.ToolMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a player kills a mob using a VulcanTools mob sword
 */
public class MobKillEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Entity killedEntity;
    private final EntityType entityType;
    private final EntityDeathEvent originalEvent;
    private final ToolMode toolMode;
    private double amountKilled;
    private boolean cancelled;

    /**
     * Creates a new MobKillEvent.
     *
     * @param player the player who killed the mob
     * @param killedEntity the entity that was killed
     * @param originalEvent the original Bukkit EntityDeathEvent
     * @param toolMode the tool mode of the mob sword used
     * @param amountKilled the amount of mobs killed
     */
    public MobKillEvent(Player player, Entity killedEntity, EntityDeathEvent originalEvent, ToolMode toolMode, int amountKilled) {
        this.player = player;
        this.killedEntity = killedEntity;
        this.entityType = killedEntity.getType();
        this.originalEvent = originalEvent;
        this.toolMode = toolMode;
        this.amountKilled = amountKilled;
        this.cancelled = false;
    }

    /**
     * Gets the amount of mobs killed.
     *
     * @return the amount of mobs killed
     */
    public double getAmountKilled() {return amountKilled;}

    /**
     * Gets the entity that was killed.
     *
     * @return the killed entity
     */
    public Entity getKilledEntity() {return killedEntity;}

    /**
     * Gets the type of entity that was killed.
     *
     * @return the entity type
     */
    public EntityType getEntityType() {return entityType;}

    /**
     * Gets the player who killed the mob.
     *
     * @return the player who killed the mob
     */
    public Player getPlayer() {return player;}

    /**
     * Gets the tool mode of the mob sword used.
     *
     * @return the tool mode of the mob sword
     */
    public ToolMode getToolMode() {return toolMode;}

    /**
     * Gets the amount of mobs killed (alias for getAmountKilled).
     *
     * @return the amount of mobs killed
     */
    public double getKilledAmount() {return amountKilled;}

    /**
     * Gets the original Bukkit EntityDeathEvent that triggered this event.
     *
     * @return the original death event
     */
    public EntityDeathEvent getBukkitEvent() {
        return originalEvent;
    }

    /**
     * Checks if this mob kill counts towards event progress.
     *
     * @return true if it counts for events, false otherwise
     */
    public boolean countsForEvent() {
        return !cancelled;
    }

    /**
     * Sets the amount of mobs killed.
     *
     * @param amountKilled the new amount of mobs killed
     */
    public void setAmountKilled(double amountKilled) {this.amountKilled = amountKilled;}

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {this.cancelled = cancelled;}
    
    @NotNull
    @Override 
    public HandlerList getHandlers() { 
        return handlers; 
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}