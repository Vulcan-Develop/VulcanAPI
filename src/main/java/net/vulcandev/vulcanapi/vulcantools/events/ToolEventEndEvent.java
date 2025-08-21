package net.vulcandev.vulcanapi.vulcantools.events;

import net.vulcandev.vulcanapi.vulcantools.wrapper.ToolTypeWrapper;
import net.vulcandev.vulcantools.enums.ToolType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Event fired when a tool event ends
 */
public class ToolEventEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final ToolTypeWrapper toolType;
    private final LinkedHashMap<UUID, Integer> finalLeaderboard;
    private final int totalParticipants;
    private final boolean wasManuallyEnded;
    
    /**
     * Creates a new ToolEventEndEvent.
     *
     * @param toolType the type of tool event that ended
     * @param finalLeaderboard the final leaderboard with player UUIDs and their scores
     * @param wasManuallyEnded whether the event was manually ended or ended naturally
     */
    public ToolEventEndEvent(ToolType toolType, LinkedHashMap<UUID, Integer> finalLeaderboard, boolean wasManuallyEnded) {
        this.toolType = ToolTypeWrapper.fromVulcanToolType(toolType);
        this.finalLeaderboard = finalLeaderboard;
        this.totalParticipants = finalLeaderboard.size();
        this.wasManuallyEnded = wasManuallyEnded;
    }

    /**
     * Gets the type of tool event that ended.
     *
     * @return the tool event type
     */
    public ToolTypeWrapper getToolType() {
        return toolType;
    }

    /**
     * Gets the final leaderboard of the event.
     *
     * @return a LinkedHashMap with player UUIDs as keys and scores as values
     */
    public LinkedHashMap<UUID, Integer> getFinalLeaderboard() {
        return finalLeaderboard;
    }

    /**
     * Gets the total number of participants in the event.
     *
     * @return the total number of participants
     */
    public int getTotalParticipants() {
        return totalParticipants;
    }

    /**
     * Checks if the event was manually ended.
     *
     * @return true if manually ended, false if ended naturally
     */
    public boolean isWasManuallyEnded() {
        return wasManuallyEnded;
    }

    /**
     * Gets the winner of the event (player with most progress).
     *
     * @return UUID of the winner, or null if no participants
     */
    public UUID getWinner() {
        return finalLeaderboard.isEmpty() ? null : finalLeaderboard.entrySet().iterator().next().getKey();
    }

    /**
     * Gets the winning amount achieved by the winner.
     *
     * @return the amount achieved by the winner, or 0 if no participants
     */
    public int getWinningAmount() {
        return finalLeaderboard.isEmpty() ? 0 : finalLeaderboard.entrySet().iterator().next().getValue();
    }

    /**
     * Gets the top N players from the event.
     *
     * @param limit the maximum number of players to return
     * @return a LinkedHashMap of top players (UUID -> amount)
     */
    public LinkedHashMap<UUID, Integer> getTopPlayers(int limit) {
        LinkedHashMap<UUID, Integer> topPlayers = new LinkedHashMap<>();
        int count = 0;
        for (Map.Entry<UUID, Integer> entry : finalLeaderboard.entrySet()) {
            if (count >= limit) break;
            topPlayers.put(entry.getKey(), entry.getValue());
            count++;
        }
        return topPlayers;
    }

    /**
     * Checks if the event ended naturally (time ran out) or was manually stopped.
     *
     * @return true if manually ended, false if ended naturally
     */
    public boolean wasManuallyEnded() {
        return wasManuallyEnded;
    }

    /**
     * Gets a description of the event results.
     *
     * @return a string describing the event results
     */
    public String getResultsDescription() {
        if (totalParticipants == 0) {
            return toolType.getNiceName() + " event ended with no participants";
        }
        return toolType.getNiceName() + " event ended with " + totalParticipants + " participants. Winner achieved " + getWinningAmount();
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
