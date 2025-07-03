package net.vulcandev.vulcanapi.vulcanstaff.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player is reported by another player.
 * This event can be cancelled to prevent the report from being created.
 */
public class PlayerReportEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    
    private final Player reporter;
    private final String reportedPlayer;
    private final String reason;
    
    /**
     * Constructs a new PlayerReportEvent.
     * @param reporter The player making the report
     * @param reportedPlayer The name of the player being reported
     * @param reason The reason for the report
     */
    public PlayerReportEvent(Player reporter, String reportedPlayer, String reason) {
        this.reporter = reporter;
        this.reportedPlayer = reportedPlayer;
        this.reason = reason;
    }
    
    /**
     * Gets the player making the report.
     * @return The reporter
     */
    public Player getReporter() {
        return reporter;
    }
    
    /**
     * Gets the name of the player being reported.
     * @return The reported player's name
     */
    public String getReportedPlayer() {
        return reportedPlayer;
    }
    
    /**
     * Gets the reason for the report.
     * @return The report reason
     */
    public String getReason() {
        return reason;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
