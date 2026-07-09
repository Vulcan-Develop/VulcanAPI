package net.vulcandev.vulcanapi.vulcantools.events;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import org.bukkit.entity.Player;

@Getter
public class BoosterApplyEvent extends VulcanEvent implements Cancellable {
    private final Player targetPlayer;
    private final String targetTeam;
    private final String boosterType;
    private final String targetCurrency;
    @Setter
    private double multiplier;
    @Setter
    private int durationSeconds;
    private final Player appliedBy;
    @Setter
    private boolean cancelled;

    public BoosterApplyEvent(Player targetPlayer, String targetTeam, String boosterType, String targetCurrency, double multiplier, int durationSeconds, Player appliedBy) {
        this.targetPlayer = targetPlayer;
        this.targetTeam = targetTeam;
        this.boosterType = boosterType;
        this.targetCurrency = targetCurrency;
        this.multiplier = multiplier;
        this.durationSeconds = durationSeconds;
        this.appliedBy = appliedBy;
        this.cancelled = false;
    }

    public boolean isPlayerBooster() {
        return targetPlayer != null;
    }

    public boolean isTeamBooster() {
        return targetTeam != null;
    }

    public boolean isCurrencySpecific() {
        return targetCurrency != null;
    }

    public double getDurationMinutes() {
        return durationSeconds / 60.0;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
