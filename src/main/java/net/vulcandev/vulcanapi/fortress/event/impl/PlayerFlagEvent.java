package net.vulcandev.vulcanapi.fortress.event.impl;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.fortress.check.CheckType;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.fortress.player.PlayerProfile;
import org.jetbrains.annotations.ApiStatus;

@Getter
public class PlayerFlagEvent extends VulcanEvent implements Cancellable {
    private final PlayerProfile player;
    private final CheckType checkName;
    private final String checkType;
    private final String checkTypeAdvanced;
    private final String debugData;
    private final String description;
    private final int violationLevel;
    private final int maxViolationLevel;
    private final String releaseType;
    /**
     * Wall-clock time (epoch millis) of the behaviour that caused this flag. Checks that decide
     * later than the offending packet (cloud analysis, buffered heuristics) report the evidence
     * time here, so it can be earlier than the moment the event is fired.
     */
    private final long flaggedAt;

    @Setter
    private boolean cancelled = false;

    @Setter
    private boolean suppressAlert = false;

    @ApiStatus.Internal
    public PlayerFlagEvent(PlayerProfile player, CheckType checkName, String checkType, String checkTypeAdvanced, String debugData, String description, int violationLevel, int maxViolationLevel, String releaseType) {
        this(player, checkName, checkType, checkTypeAdvanced, debugData, description, violationLevel, maxViolationLevel, releaseType, System.currentTimeMillis());
    }

    public PlayerFlagEvent(PlayerProfile player, CheckType checkName, String checkType, String checkTypeAdvanced, String debugData, String description, int violationLevel, int maxViolationLevel, String releaseType, long flaggedAt) {
        this.player = player;
        this.checkName = checkName;
        this.checkType = checkType;
        this.checkTypeAdvanced = checkTypeAdvanced;
        this.debugData = debugData;
        this.description = description;
        this.violationLevel = violationLevel;
        this.maxViolationLevel = maxViolationLevel;
        this.releaseType = releaseType;
        this.flaggedAt = flaggedAt > 0L ? flaggedAt : System.currentTimeMillis();
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
