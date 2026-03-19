package net.vulcandev.vulcanapi.vulcantools.events;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import org.bukkit.entity.Player;

/**
 * Event fired when a player earns currency from tool usage.
 */
@Getter
public class CurrencyGrindEvent extends VulcanEvent {
    private final Player player;
    private final String currency;
    @Setter
    private long amount;

    /**
     * Creates a new CurrencyGrindEvent.
     *
     * @param player the player who earned the currency
     * @param currency the type of currency earned
     * @param amount the amount of currency earned
     */
    public CurrencyGrindEvent(Player player, String currency, long amount) {
        this.player = player;
        this.currency = currency;
        this.amount = amount;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }
}
