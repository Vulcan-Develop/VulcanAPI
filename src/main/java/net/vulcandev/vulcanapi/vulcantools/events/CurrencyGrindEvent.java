package net.vulcandev.vulcanapi.vulcantools.events;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import org.bukkit.entity.Player;

@Getter
public class CurrencyGrindEvent extends VulcanEvent {
    private final Player player;
    private final String currency;
    @Setter
    private long amount;

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
