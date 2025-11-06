package net.vulcandev.vulcanapi.events.interfaces;

/**
 * Interface for player ban to avoid direct class dependencies
 * This allows VulcanAPI to work with bans across classloader boundaries
 */
public interface IPlayerBan {

    /**
     * Checks if the ban is still active
     * @return true if still banned, false otherwise
     */
    boolean isStillBanned();
}
