package net.vulcandev.vulcanapi.vulcangift.events;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import org.bukkit.entity.Player;

/**
 * Fired when a player gifts an item to another player, before the gift is handed over.
 * This is the only point at which the sender is known - gifts are persisted against the
 * recipient alone.
 */
@Getter
public class GiftSendEvent extends VulcanEvent implements Cancellable {
    private final Player sender;
    private final Player recipient;
    private final GiftItemSnapshot gift;

    public GiftSendEvent(Player sender, Player recipient, GiftItemSnapshot gift) {
        this.sender = sender;
        this.recipient = recipient;
        this.gift = gift;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
