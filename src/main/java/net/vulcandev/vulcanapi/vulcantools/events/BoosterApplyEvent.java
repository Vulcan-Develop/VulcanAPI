package net.vulcandev.vulcanapi.vulcantools.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a booster is applied to a player or team
 */
public class BoosterApplyEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player targetPlayer; // null if team booster
    private final String targetTeam; // null if player booster
    private final String boosterType;
    private final String targetCurrency; // null if applies to all
    private double multiplier;
    private int durationSeconds;
    private final Player appliedBy; // who applied the booster
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
    public BoosterApplyEvent(Player targetPlayer, String targetTeam, String boosterType, String targetCurrency,
                            double multiplier, int durationSeconds, Player appliedBy) {
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
     * Gets the target player for this booster.
     *
     * @return the target player, or null if this is a team booster
     */
    public Player getTargetPlayer() {
        return targetPlayer;
    }

    /**
     * Gets the target team for this booster.
     *
     * @return the target team name, or null if this is a player booster
     */
    public String getTargetTeam() {
        return targetTeam;
    }

    /**
     * Gets the type of booster being applied.
     *
     * @return the booster type
     */
    public String getBoosterType() {
        return boosterType;
    }

    /**
     * Gets the target currency for this booster.
     *
     * @return the target currency, or null if applies to all currencies
     */
    public String getTargetCurrency() {
        return targetCurrency;
    }

    /**
     * Gets the multiplier value of the booster.
     *
     * @return the multiplier value
     */
    public double getMultiplier() {
        return multiplier;
    }

    /**
     * Sets the multiplier value of the booster.
     *
     * @param multiplier the new multiplier value
     */
    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * Gets the duration of the booster in seconds.
     *
     * @return the duration in seconds
     */
    public int getDurationSeconds() {
        return durationSeconds;
    }

    /**
     * Sets the duration of the booster in seconds.
     *
     * @param durationSeconds the new duration in seconds
     */
    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    /**
     * Gets the player who applied this booster.
     *
     * @return the player who applied the booster
     */
    public Player getAppliedBy() {
        return appliedBy;
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

    /**
     * Gets a description of the booster target.
     *
     * @return a string describing who/what the booster targets
     */
    public String getTargetDescription() {
        if (isPlayerBooster()) {
            return "Player: " + targetPlayer.getName();
        } else if (isTeamBooster()) {
            return "Team: " + targetTeam;
        }
        return "Unknown";
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    @NotNull
    @Override 
    public HandlerList getHandlers() { 
        return handlers; 
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
