package net.vulcandev.vulcanapi.vulcancrates.events;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Event fired when a player opens a crate and a prize has been rolled.
 * This event is informational and is dispatched before reward commands execute.
 */
@Getter
public class CrateOpenEvent extends VulcanEvent {
    private final Player player;
    private final CrateSnapshot crate;
    private final PrizeSnapshot prize;

    public CrateOpenEvent(Player player, CrateSnapshot crate, PrizeSnapshot prize) {
        this.player = player;
        this.crate = crate;
        this.prize = prize;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }

    @Getter
    public static final class CrateSnapshot {
        private final String name;
        private final String displayName;

        public CrateSnapshot(String name, String displayName) {
            this.name = name;
            this.displayName = displayName;
        }
    }

    @Getter
    public static final class PrizeSnapshot {
        private final String name;
        private final List<String> commands;
        private final double chance;
        private final boolean announce;
        private final String url;
        private final Material material;
        private final int amount;
        private final byte data;
        private final List<String> lore;
        private final boolean glowing;

        public PrizeSnapshot(String name, List<String> commands, double chance, boolean announce, String url,
                             Material material, int amount, byte data, List<String> lore, boolean glowing) {
            this.name = name;
            this.commands = immutableCopy(commands);
            this.chance = chance;
            this.announce = announce;
            this.url = url;
            this.material = material;
            this.amount = amount;
            this.data = data;
            this.lore = immutableCopy(lore);
            this.glowing = glowing;
        }

        private static List<String> immutableCopy(List<String> values) {
            if (values == null || values.isEmpty()) {
                return Collections.emptyList();
            }
            return Collections.unmodifiableList(new ArrayList<>(values));
        }
    }
}
