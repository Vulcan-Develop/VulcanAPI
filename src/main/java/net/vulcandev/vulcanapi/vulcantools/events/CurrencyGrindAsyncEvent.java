package net.vulcandev.vulcanapi.vulcantools.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a player earns currency from tool usage.
 */
public class CurrencyGrindAsyncEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String currency;
    private long amount;

    /**
     * Creates a new CurrencyGrindAsyncEvent.
     *
     * @param player the player who earned the currency
     * @param currency the type of currency earned
     * @param amount the amount of currency earned
     */
    public CurrencyGrindAsyncEvent(Player player, String currency, long amount) {
        super(true); // Mark as async event
        this.player = player;
        this.currency = currency;
        this.amount = amount;
    }

    /**
     * Gets the player who earned the currency.
     *
     * @return the player who earned the currency
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the type of currency that was earned.
     *
     * @return the currency type
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Gets the amount of currency earned.
     *
     * @return the amount of currency earned
     */
    public long getAmount() {
        return amount;
    }

    /**
     * Sets the amount of currency earned.
     *
     * @param amount the new amount of currency earned
     */
    public void setAmount(long amount) {
        this.amount = amount;
    }

    @NotNull
    @Override public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
