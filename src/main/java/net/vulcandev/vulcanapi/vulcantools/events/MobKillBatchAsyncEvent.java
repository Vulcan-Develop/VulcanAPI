package net.vulcandev.vulcanapi.vulcantools.events;

import net.vulcandev.vulcanapi.wrapper.ToolModeWrapper;
import net.vulcandev.vulcantools.enums.ToolMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * Event fired when a player kills a mob using a Mob Sword
 * Note: This event is fired asynchronously as part of the batch processing system
 */
public class MobKillBatchAsyncEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Entity killedEntity;
    private final EntityDeathEvent originalEvent;
    private final ToolModeWrapper toolMode;
    private final int amountKilled;

    public MobKillBatchAsyncEvent(Player player, Entity killedEntity, EntityDeathEvent originalEvent, ToolMode toolMode, int amountKilled) {
        super(true); // Mark as async event
        this.player = player;
        this.killedEntity = killedEntity;
        this.originalEvent = originalEvent;
        this.toolMode = ToolModeWrapper.fromVulcanToolMode(toolMode);
        this.amountKilled = amountKilled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public int getAmountKilled() {
        return amountKilled;
    }

    public Entity getKilledEntity() {
        return killedEntity;
    }

    public EntityDeathEvent getOriginalEvent() {
        return originalEvent;
    }

    public Player getPlayer() {
        return player;
    }

    public ToolModeWrapper getToolMode() {
        return toolMode;
    }
}
