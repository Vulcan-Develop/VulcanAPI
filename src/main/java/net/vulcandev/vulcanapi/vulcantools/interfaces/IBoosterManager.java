package net.vulcandev.vulcanapi.vulcantools.interfaces;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

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
     * Applies a booster to a player with a caller-supplied tracking UUID so it can later be
     * identified, refreshed, or removed by that UUID.
     * <p>
     * Boosters are additive: applying another booster of the same type stacks its multiplier.
     * To avoid stacking, remove any previous booster (via {@link #removeBooster(UUID)} or
     * {@link #removeBooster(Player, String)}) before re-applying.
     *
     * @param player the player to apply the booster to
     * @param boosterType the type of booster (e.g., "currencybooster")
     * @param target the target of the booster (e.g., currency name), or null for all targets
     * @param multiplier the multiplier value
     * @param durationSeconds the duration in seconds
     * @param uuid the tracking UUID to assign, or null to generate a new one
     * @return the tracking UUID that was assigned to the booster
     */
    UUID applyBooster(Player player, String boosterType, String target, double multiplier, int durationSeconds, UUID uuid);

    /**
     * Applies a booster to many online players at once, all sharing a single group UUID.
     * Removing that UUID via {@link #removeBooster(UUID)} removes the booster from every member
     * at once (e.g. when a faction loses control of an outpost).
     *
     * @param players the players to apply the booster to
     * @param boosterType the type of booster
     * @param target the target of the booster, or null for all targets
     * @param multiplier the multiplier value
     * @param durationSeconds the duration in seconds
     * @param uuid the shared group UUID to assign, or null to generate a new one
     * @return the shared group UUID that was assigned to every applied booster
     */
    UUID applyBoosters(List<Player> players, String boosterType, String target, double multiplier, int durationSeconds, UUID uuid);

    /**
     * Applies a booster to many players referenced by their Minecraft UUID, all sharing a single
     * group UUID. Online players are resolved directly; offline players are resolved by name when
     * available and otherwise skipped.
     * <p>
     * Named distinctly from {@link #applyBoosters(List, String, String, double, int, UUID)}
     * because {@code List&lt;Player&gt;} and {@code List&lt;UUID&gt;} are indistinguishable after erasure.
     *
     * @param playerUuids the Minecraft UUIDs of the players to apply the booster to
     * @param boosterType the type of booster
     * @param target the target of the booster, or null for all targets
     * @param multiplier the multiplier value
     * @param durationSeconds the duration in seconds
     * @param uuid the shared group UUID to assign, or null to generate a new one
     * @return the shared group UUID that was assigned to every applied booster
     */
    UUID applyBoostersByUUID(List<UUID> playerUuids, String boosterType, String target, double multiplier, int durationSeconds, UUID uuid);

    /**
     * Looks up a booster by its tracking UUID.
     * <p>
     * The return type is {@link Object} because the concrete booster class lives in VulcanTools;
     * pass the result to {@link #getBoosterUUID(Object)} to read its tracking UUID. When the UUID
     * is a shared group UUID, the first active booster in the group is returned.
     *
     * @param uuid the tracking UUID
     * @return the matching booster as an {@link Object}, or null if none is active
     */
    Object getPlayerBooster(UUID uuid);

    /**
     * Returns the tracking UUID of a booster object previously obtained from this manager.
     *
     * @param booster a booster object (e.g. from {@link #getPlayerBooster(UUID)})
     * @return the booster's tracking UUID, or null if the argument is not a booster
     */
    UUID getBoosterUUID(Object booster);

    /**
     * Removes all of a player's active player-scoped boosters of the given type. Team and global
     * boosters are not affected.
     *
     * @param player the player whose boosters should be removed
     * @param boosterType the type of booster to remove
     * @return true if at least one booster was removed, false otherwise
     */
    boolean removeBooster(Player player, String boosterType);

    /**
     * Removes every booster sharing the given tracking/group UUID, regardless of which players or
     * teams they belong to.
     *
     * @param uuid the tracking/group UUID
     * @return true if at least one booster was removed, false otherwise
     */
    boolean removeBooster(UUID uuid);
}
