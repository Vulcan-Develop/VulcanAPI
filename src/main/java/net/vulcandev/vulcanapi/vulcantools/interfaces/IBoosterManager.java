package net.vulcandev.vulcanapi.vulcantools.interfaces;

import org.bukkit.entity.Player;

/**
 * Interface for managing boosters in VulcanTools.
 * This interface provides access to booster operations for external plugins.
 */
public interface IBoosterManager {

    /**
     * Gets the total multiplier for a player with a specific booster type and target.
     *
     * @param player the player to check
     * @param boosterType the type of booster (e.g., "currencybooster", "enchantbooster")
     * @param target the target of the booster (e.g., currency name, enchant name)
     * @return the total multiplier value
     */
    double getTotalMultiplier(Player player, String boosterType, String target);

    /**
     * Applies a booster to a player.
     *
     * @param player the player to apply the booster to
     * @param boosterType the type of booster
     * @param target the target of the booster
     * @param multiplier the multiplier value
     * @param durationSeconds the duration in seconds
     */
    void applyBooster(Player player, String boosterType, String target, double multiplier, int durationSeconds);

    /**
     * Checks if a player has an active booster of a specific type and target.
     *
     * @param player the player to check
     * @param boosterType the type of booster
     * @param target the target of the booster
     * @return true if the player has an active booster, false otherwise
     */
    boolean hasActiveBooster(Player player, String boosterType, String target);

    /**
     * Removes a booster from a player.
     *
     * @param player the player to remove the booster from
     * @param boosterType the type of booster
     * @param target the target of the booster
     */
    void removeBooster(Player player, String boosterType, String target);

    /**
     * Gets all active boosters for a player (including team boosters).
     *
     * @param player the player to check
     * @return a list of active booster descriptions
     */
    java.util.List<String> getActiveBoosterDescriptions(Player player);
}
