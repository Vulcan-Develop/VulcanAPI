package net.vulcandev.vulcanapi.vulcantools.events;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import org.bukkit.entity.Player;

/**
 * Event fired when a booster is applied to a player or team
 */
@Getter
public class BoosterApplyEvent extends VulcanEvent implements Cancellable {
    private final Player targetPlayer; // null if team booster
    private final String targetTeam; // null if player booster
    private final String boosterType;
    private final String targetCurrency; // null if applies to all
    @Setter
    private double multiplier;
    @Setter
    private int durationSeconds;
    private final Player appliedBy; // who applied the booster
    @Setter
    private boolean cancelled;
    
    /**
     * Creates a new BoosterApplyEvent.
     *
     * @param targetPlayer the player to apply the booster to (null if team booster)
     * @param targetTeam the team to apply the booster to (null if player booster)
     * @param boosterType the type of booster being applied
     * @param targetCurrency the currency this booster affects (null if applies to all)
     * @param multiplier the multiplier value of the booster
     * @param durationSeconds the duration of the booster in seconds
     * @param appliedBy the player who applied the booster
     */
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

    /**
     * Checks if this is a player-specific booster.
     *
     * @return true if targeting a specific player, false if team booster
     */
    public boolean isPlayerBooster() {
        return targetPlayer != null;
    }

    /**
     * Checks if this is a team-specific booster.
     *
     * @return true if targeting a team, false if player booster
     */
    public boolean isTeamBooster() {
        return targetTeam != null;
    }

    /**
     * Checks if this booster applies to a specific currency.
     *
     * @return true if currency-specific, false if applies to all currencies
     */
    public boolean isCurrencySpecific() {
        return targetCurrency != null;
    }

    /**
     * Gets the duration of the booster in minutes.
     *
     * @return the duration in minutes
     */
    public double getDurationMinutes() {
        return durationSeconds / 60.0;
    }
    @Override
    public boolean isCancellable() {
        return true;
    }
}
