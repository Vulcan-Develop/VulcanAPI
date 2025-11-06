package net.vulcandev.vulcanapi.events.interfaces;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * Interface for event instances to avoid direct class dependencies
 * This allows VulcanAPI to work with events across classloader boundaries
 */
public interface IEventInstance {

    /**
     * Gets the name of the event
     * @return the event name
     */
    @NotNull
    String getName();

    /**
     * Gets the event type as a string
     * @return the event type name (e.g. "PARKOUR", "SUMO")
     */
    @NotNull
    String getEventTypeName();

    /**
     * Gets the event state as a string
     * @return the event state name (e.g. "WAITING", "RUNNING")
     */
    @NotNull
    String getStateName();

    /**
     * Gets the seconds remaining in the event
     * @return seconds remaining
     */
    int getSecondsLeft();

    /**
     * Checks if a player is participating in the event
     * @param uuid the player's UUID
     * @return true if participating, false otherwise
     */
    boolean isParticipating(UUID uuid);

    /**
     * Checks if there is space for more participants
     * @return true if there is space, false otherwise
     */
    boolean isSpace();

    /**
     * Gets all participants as a map of UUID to Player
     * @return map of participants (only Bukkit Player objects, no custom types)
     */
    @NotNull
    Map<UUID, Player> getPlayerMap();

    /**
     * Gets all spectators as a map of UUID to Player
     * @return map of spectators (only Bukkit Player objects, no custom types)
     */
    @NotNull
    Map<UUID, Player> getSpectatorMap();
}
