package net.vulcandev.vulcanapi.vulcanstaff;

import net.vulcandev.staff.VulcanStaff;
import net.vulcandev.staff.features.freeze.FreezeFeature;
import net.vulcandev.staff.features.staffmode.StaffModeFeature;
import net.vulcandev.staff.features.vanish.VanishFeature;
import net.vulcandev.vulcanapi.vulcanstaff.events.PlayerFreezeEvent;
import net.vulcandev.vulcanapi.vulcanstaff.events.StaffVanishEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

/**
 * Main API class for external plugins to interact with VulcanStaff.
 * Provides essential methods for vanish, staff mode, and freeze functionality.
 */
public class VulcanStaffAPI {
    private static VulcanStaffAPI instance;
    private final VulcanStaff plugin;

    public VulcanStaffAPI(VulcanStaff plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets the API instance
     * @return VulcanStaffAPI instance
     */
    public static VulcanStaffAPI getInstance() {return instance;}

    /**
     * Check if VulcanStaff is available and loaded
     * @return true if VulcanStaff is available, false otherwise
     */
    public static boolean isAvailable() {return instance != null && instance.plugin != null && instance.plugin.isEnabled();}

    // ===== VANISH API =====

    /**
     * Checks if a player is vanished.
     * @param player The player to check
     * @return True if the player is vanished
     */
    public boolean isVanished(Player player) {
        return isVanished(player.getUniqueId());
    }

    /**
     * Checks if a player is vanished by UUID.
     * @param uuid The player's UUID
     * @return True if the player is vanished
     */
    public boolean isVanished(UUID uuid) {
        return isAvailable() && VanishFeature.getInstance().getVanish().isVanished(uuid);
    }

    /**
     * Sets a player's vanish state.
     * @param player The player to vanish/unvanish
     * @param vanished True to vanish, false to unvanish
     * @return True if the operation was successful
     */
    public boolean setVanished(Player player, boolean vanished) {
        if (!isAvailable()) return false;

        // Fire event
        StaffVanishEvent event = new StaffVanishEvent(player, vanished, StaffVanishEvent.VanishReason.API);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;

        VanishFeature vanishFeature = VanishFeature.getInstance();
        if (vanished) {
            vanishFeature.getVanish().vanish(player);
        } else {
            vanishFeature.getVanish().unVanish(player);
        }
        return true;
    }

    /**
     * Checks if a player can see vanished players.
     * @param player The player to check
     * @return True if the player can see vanished players
     */
    public boolean canSeeVanished(Player player) {
        return isAvailable() && VanishFeature.getInstance().canSeeVanished(player);
    }

    /**
     * Gets all currently vanished players.
     * @return Set of UUIDs of vanished players
     */
    public Set<UUID> getVanishedPlayers() {
        return isAvailable() ? VanishFeature.getInstance().getVanish().getVanishedPlayers() : Collections.emptySet();
    }

    // ===== STAFF MODE API =====

    /**
     * Checks if a player is in staff mode.
     * @param player The player to check
     * @return True if the player is in staff mode
     */
    public boolean isInStaffMode(Player player) {
        return isInStaffMode(player.getUniqueId());
    }

    /**
     * Checks if a player is in staff mode by UUID.
     * @param uuid The player's UUID
     * @return True if the player is in staff mode
     */
    public boolean isInStaffMode(UUID uuid) {
        return isAvailable() && StaffModeFeature.getInstance().getStaffModeList().containsKey(uuid);
    }

    // ===== FREEZE API =====

    /**
     * Checks if a player is frozen.
     * @param player The player to check
     * @return True if the player is frozen
     */
    public boolean isFrozen(Player player) {
        return isFrozen(player.getUniqueId());
    }

    /**
     * Checks if a player is frozen by UUID.
     * @param uuid The player's UUID
     * @return True if the player is frozen
     */
    public boolean isFrozen(UUID uuid) {
        return isAvailable() && FreezeFeature.getInstance().getFrozenPlayers().contains(uuid);
    }

    /**
     * Sets a player's freeze state.
     * @param target The player to freeze/unfreeze
     * @param staff The staff member performing the action (can be null)
     * @param frozen True to freeze, false to unfreeze
     * @return True if the operation was successful
     */
    public boolean setFrozen(Player target, Player staff, boolean frozen) {
        if (!isAvailable()) return false;

        // Fire event
        PlayerFreezeEvent event = new PlayerFreezeEvent(target, staff, frozen, PlayerFreezeEvent.FreezeReason.API);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;

        FreezeFeature freezeFeature = FreezeFeature.getInstance();
        if (frozen) {
            freezeFeature.addFrozenPlayer(target.getUniqueId());
        } else {
            freezeFeature.removeFrozenPlayer(target.getUniqueId());
        }
        return true;
    }

    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin.getClass().getName().equals("net.vulcandev.staff.VulcanStaff")) {
            VulcanStaff vulcanStaffPlugin = (VulcanStaff) plugin;
            instance = new VulcanStaffAPI(vulcanStaffPlugin);
        }
    }

    /**
     * Clean up the API instance
     */
    public static void cleanup() {
        instance = null;
    }
}
