package net.vulcandev.vulcanapi.vulcanpass.events;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fired when a pass reward is claimed, before its commands are dispatched.
 * {@code passType} is "basic" or "premium".
 */
@Getter
public class PassRewardClaimEvent extends VulcanEvent implements Cancellable {
    private final Player player;
    private final int level;
    private final String passType;
    private final String rewardName;
    private final List<String> commands;

    public PassRewardClaimEvent(Player player, int level, String passType, String rewardName, List<String> commands) {
        this.player = player;
        this.level = level;
        this.passType = passType;
        this.rewardName = rewardName;
        this.commands = commands == null || commands.isEmpty()
                ? Collections.<String>emptyList()
                : Collections.unmodifiableList(new ArrayList<String>(commands));
    }

    public boolean isPremium() {
        return "premium".equalsIgnoreCase(passType);
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
