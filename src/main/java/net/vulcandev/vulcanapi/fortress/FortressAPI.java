package net.vulcandev.vulcanapi.fortress;

import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.event.VulcanListener;
import net.vulcandev.vulcanapi.fortress.player.PlayerProfile;

import java.util.Collection;
import java.util.UUID;

/**
 * Main API interface for Fortress Anticheat
 * This will be implemented by the actual anticheat plugin
 */
public interface FortressAPI {

    /**
     * Register an event listener
     */
    void registerListener(VulcanListener listener);

    /**
     * Unregister an event listener
     */
    void unregisterListener(VulcanListener listener);

    /**
     * Call an event (used internally by the anticheat)
     */
    boolean callEvent(VulcanEvent event);

    /**
     * Get a player's profile
     */
    PlayerProfile getPlayerProfile(UUID uuid);

    /**
     * Get all online player profiles
     */
    Collection<PlayerProfile> getOnlineProfiles();

    /**
     * Check if a player is being monitored
     */
    boolean isPlayerMonitored(UUID uuid);

    /**
     * Get API version
     */
    String getVersion();

    /**
     * Check if the anticheat is enabled
     */
    boolean isEnabled();

    static FortressAPI getInstance() {
        return FortressAPIInstance.getInstance();
    }

    class FortressAPIInstance {
        private static FortressAPI fortressAPI;

        /**
         * @return the instance
         */
        public static FortressAPI getInstance() {
            return fortressAPI;
        }

        /**
         * Sets the ExecutionerAPI instance.
         *
         * @param v the server
         */
        public static void setInstance(FortressAPI v) {
            fortressAPI = v;
        }
    }
}