package net.vulcandev.vulcanapi.vulcangift.events;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Fired when a pending gift is claimed, before the item reaches the claimer's inventory.
 * {@code owner} is the player whose gift list the item came from; staff viewing another
 * player's gifts means it does not always match the claimer.
 */
@Getter
public class GiftClaimEvent extends VulcanEvent implements Cancellable {
    private final Player claimer;
    private final UUID owner;
    private final GiftItemSnapshot gift;

    public GiftClaimEvent(Player claimer, UUID owner, GiftItemSnapshot gift) {
        this.claimer = claimer;
        this.owner = owner;
        this.gift = gift;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
