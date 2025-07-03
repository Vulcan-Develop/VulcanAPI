package net.vulcandev.vulcanapi.vulcantools.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a player harvests sugar cane using a VulcanTools harvester hoe.
 */
public class CaneHarvestEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final int amount;

    /**
     * Creates a new CaneHarvestEvent.
     *
     * @param player the player who harvested the sugar cane
     * @param amount the amount of sugar cane harvested
     */
    public CaneHarvestEvent(Player player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    /**
     * Gets the player who harvested the sugar cane.
     *
     * @return the player who harvested the sugar cane
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the amount of sugar cane harvested.
     *
     * @return the amount of sugar cane harvested
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Gets the amount of sugar cane harvested (alias for getAmount).
     *
     * @return the amount of sugar cane harvested
     */
    public int getHarvestedAmount() {
        return amount;
    }

    @NotNull
    @Override public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
