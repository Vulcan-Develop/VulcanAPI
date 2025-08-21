package net.vulcandev.vulcanapi.vulcantools.interfaces;

import net.vulcandev.vulcanapi.wrapper.ToolTypeWrapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 * Interface for managing tool events in VulcanTools.
 * This interface provides access to event operations for external plugins.
 */
public interface IEventManager {

    /**
     * Starts a tool event.
     *
     * @param eventType the type of tool event to start
     * @param durationSeconds the duration of the event in seconds
     */
    void startEvent(ToolTypeWrapper eventType, int durationSeconds);

    /**
     * Ends the current active event.
     */
    void endEvent();

    /**
     * Checks if an event is currently active.
     *
     * @return true if an event is active, false otherwise
     */
    boolean isEventActive();

    /**
     * Gets the current event type.
     *
     * @return the current event type, or null if no event is active
     */
    ToolTypeWrapper getEventType();

    /**
     * Adds harvest amount for a player during an event.
     *
     * @param playerUUID the player's UUID
     * @param amount the amount to add
     * @param toolType the tool type used
     */
    void addHarvest(UUID playerUUID, int amount, ToolTypeWrapper toolType);

    /**
     * Gets the harvest amount for a specific player.
     *
     * @param playerUUID the player's UUID
     * @return the player's harvest amount
     */
    int getHarvestAmount(UUID playerUUID);

    /**
     * Gets all players sorted by their harvest amounts (descending order).
     *
     * @return a LinkedHashMap with player UUIDs as keys and harvest amounts as values
     */
    LinkedHashMap<UUID, Integer> getSortedByHarvestedAmount();

    /**
     * Gets the top players in the current event.
     *
     * @param limit the maximum number of players to return
     * @return a list of player names and their harvest amounts
     */
    List<String> getTopPlayers(int limit);
}
