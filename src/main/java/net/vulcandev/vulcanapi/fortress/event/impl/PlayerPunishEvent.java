package net.vulcandev.vulcanapi.fortress.event.impl;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.fortress.check.CheckType;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.fortress.player.PlayerProfile;
import org.jetbrains.annotations.ApiStatus;

@Getter
public class PlayerPunishEvent extends VulcanEvent implements Cancellable {
    private final PlayerProfile player;
    private final CheckType checkName;
    private final String checkType;
    private final String checkTypeAdvanced;
    private final String debugData;
    private final String description;
    private final int violationLevel;
    private final int maxViolationLevel;
    /** Wall-clock time (epoch millis) of the violation that triggered the punishment. */
    private final long flaggedAt;

    @Setter
    private boolean cancelled = false;

    @Setter
    private String customPunishCommand;

    @ApiStatus.Internal
    public PlayerPunishEvent(PlayerProfile player, CheckType checkName, String checkType, String checkTypeAdvanced, String debugData, String description, int violationLevel, int maxViolationLevel) {
        this(player, checkName, checkType, checkTypeAdvanced, debugData, description, violationLevel, maxViolationLevel, System.currentTimeMillis());
    }

    public PlayerPunishEvent(PlayerProfile player, CheckType checkName, String checkType, String checkTypeAdvanced, String debugData, String description, int violationLevel, int maxViolationLevel, long flaggedAt) {
        this.player = player;
        this.checkName = checkName;
        this.checkType = checkType;
        this.checkTypeAdvanced = checkTypeAdvanced;
        this.debugData = debugData;
        this.description = description;
        this.violationLevel = violationLevel;
        this.maxViolationLevel = maxViolationLevel;
        this.flaggedAt = flaggedAt > 0L ? flaggedAt : System.currentTimeMillis();
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}