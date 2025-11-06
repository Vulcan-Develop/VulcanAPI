package net.vulcandev.vulcanapi.events.interfaces;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Interface for VulcanEvents plugin to avoid direct class dependencies
 * This allows VulcanAPI to work with VulcanEvents across classloader boundaries
 */
public interface IVulcanEventsPlugin {

    /**
     * Gets the current active event instance
     * @return the current event, or null if no event is active
     */
    @Nullable
    IEventInstance getCurrentEvent();

    /**
     * Gets a player's ban information
     * @param uuid the player's UUID
     * @return the ban object, or null if not banned
     */
    @Nullable
    IPlayerBan getPlayerBan(UUID uuid);
}
