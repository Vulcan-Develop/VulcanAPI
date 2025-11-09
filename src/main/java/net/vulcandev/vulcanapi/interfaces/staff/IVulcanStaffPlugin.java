package net.vulcandev.vulcanapi.interfaces.staff;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * Interface for VulcanStaff plugin to avoid direct class dependencies
 * This allows VulcanAPI to work with VulcanStaff across classloader boundaries
 */
public interface IVulcanStaffPlugin {

    /**
     * Checks if a player is vanished by UUID
     * @param uuid the player's UUID
     * @return true if the player is vanished
     */
    boolean isVanished(@NotNull UUID uuid);

    /**
     * Sets a player's vanish state
     * @param uuid the player's UUID
     * @param vanished true to vanish, false to unvanish
     */
    void setVanished(@NotNull UUID uuid, boolean vanished);

    /**
     * Gets all currently vanished players
     * @return Set of UUIDs of vanished players
     */
    @NotNull
    Set<UUID> getVanishedPlayers();

    /**
     * Checks if a player can see vanished players
     * @param uuid the player's UUID
     * @return true if the player can see vanished players
     */
    boolean canSeeVanished(@NotNull UUID uuid);

    /**
     * Checks if a player is in staff mode by UUID
     * @param uuid the player's UUID
     * @return true if the player is in staff mode
     */
    boolean isInStaffMode(@NotNull UUID uuid);

    /**
     * Checks if a player is frozen by UUID
     * @param uuid the player's UUID
     * @return true if the player is frozen
     */
    boolean isFrozen(@NotNull UUID uuid);

    /**
     * Sets a player's freeze state
     * @param uuid the player's UUID
     * @param frozen true to freeze, false to unfreeze
     */
    void setFrozen(@NotNull UUID uuid, boolean frozen);
}
