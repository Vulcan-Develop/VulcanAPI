package net.vulcandev.vulcanapi.vulcantools.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a player harvests sugar cane using a Harvester Hoe
 * Note: This event is fired asynchronously as part of the batch processing system in the plugin
 */
public class CaneHarvestBatchAsyncEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    
    private final Player player;
    private final int amount;

    public CaneHarvestBatchAsyncEvent(Player player, int amount) {
        super(true);
        this.player = player;
        this.amount = amount;
    }

    @Override @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {return player;}

    public int getAmount() {return amount;}
}
