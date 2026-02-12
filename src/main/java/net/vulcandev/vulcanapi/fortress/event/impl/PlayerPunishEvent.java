package net.vulcandev.vulcanapi.fortress.event.impl;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.fortress.check.CheckType;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.fortress.player.PlayerProfile;

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

    @Setter
    private boolean cancelled = false;

    @Setter
    private String customPunishCommand; // Allow plugins to override punishment

    public PlayerPunishEvent(PlayerProfile player, CheckType checkName, String checkType,
                             String checkTypeAdvanced, String debugData, String description,
                             int violationLevel, int maxViolationLevel) {
        this.player = player;
        this.checkName = checkName;
        this.checkType = checkType;
        this.checkTypeAdvanced = checkTypeAdvanced;
        this.debugData = debugData;
        this.description = description;
        this.violationLevel = violationLevel;
        this.maxViolationLevel = maxViolationLevel;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}