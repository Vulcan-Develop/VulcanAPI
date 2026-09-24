package net.vulcandev.vulcanapi.vulcanpass.events;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired after a pass XP gain pushes a player past one or more level thresholds.
 * A single gain can cross several levels, so {@code newLevel - oldLevel} is not always one.
 */
@Getter
public class PassLevelUpEvent extends VulcanEvent {
    private final Player player;
    private final int oldLevel;
    private final int newLevel;
    private final int claimableRewards;

    @ApiStatus.Internal
    public PassLevelUpEvent(Player player, int oldLevel, int newLevel, int claimableRewards) {
        this.player = player;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
        this.claimableRewards = claimableRewards;
    }

    public int getLevelsGained() {
        return newLevel - oldLevel;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }
}
