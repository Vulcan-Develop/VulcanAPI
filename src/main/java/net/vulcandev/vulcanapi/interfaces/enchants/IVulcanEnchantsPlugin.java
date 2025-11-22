package net.vulcandev.vulcanapi.interfaces.enchants;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * Interface for VulcanEnchants plugin to avoid direct class dependencies
 * This allows VulcanAPI to work with VulcanEnchants across classloader boundaries
 */
public interface IVulcanEnchantsPlugin {

    /**
     * Gets an enchant wrapper by its key/name
     * @param key the enchant key
     * @return IEnchantWrapper containing enchant information, or null if not found
     */
    @Nullable
    IEnchantWrapper getEnchant(@NotNull String key);

    /**
     * Gets all registered enchant keys
     * @return Set of all enchant keys
     */
    @NotNull
    Set<String> getAllEnchantKeys();

    /**
     * Gets all registered enchants as wrappers
     * @return List of IEnchantWrappers
     */
    @NotNull
    List<IEnchantWrapper> getAllEnchants();

    /**
     * Gets all potion enchants
     * @return List of potion enchant wrappers
     */
    @NotNull
    List<IEnchantWrapper> getPotionEnchants();

    /**
     * Gets all custom/extra enchants
     * @return List of custom enchant wrappers
     */
    @NotNull
    List<IEnchantWrapper> getCustomEnchants();

    /**
     * Checks if an enchant exists
     * @param key the enchant key
     * @return true if the enchant exists
     */
    boolean enchantExists(@NotNull String key);

    /**
     * Checks if an enchant is enabled
     * @param key the enchant key
     * @return true if the enchant is enabled
     */
    boolean isEnchantEnabled(@NotNull String key);

    /**
     * Gets the XP cost of an enchant
     * @param key the enchant key
     * @return the XP cost, or -1 if enchant not found
     */
    int getEnchantCost(@NotNull String key);

    /**
     * Gets the book item for an enchant
     * @param key the enchant key
     * @return the enchant book ItemStack, or null if not found
     */
    @Nullable
    ItemStack getEnchantBook(@NotNull String key);

    /**
     * Checks if an enchant is a potion enchant
     * @param key the enchant key
     * @return true if it's a potion enchant
     */
    boolean isPotionEnchant(@NotNull String key);

    /**
     * Gets the potion effect type name for a potion enchant
     * @param key the enchant key
     * @return the potion effect type name, or null if not a potion enchant
     */
    @Nullable
    String getPotionEffectType(@NotNull String key);

    /**
     * Gets the amplifier level for a potion enchant
     * @param key the enchant key
     * @return the amplifier level, or -1 if not found
     */
    int getPotionAmplifier(@NotNull String key);

    /**
     * Checks if an item has a specific enchant applied to it
     * @param item the item to check
     * @param key the enchant key
     * @return true if the item has the enchant
     */
    boolean itemHasEnchant(@NotNull ItemStack item, @NotNull String key);

    /**
     * Gets all enchants applied to an item
     * @param item the item to check
     * @return Set of enchant keys that are on this item
     */
    @NotNull
    Set<String> getItemEnchants(@NotNull ItemStack item);

    /**
     * Checks if an enchant can be applied to an item
     * @param item the item to check
     * @param key the enchant key
     * @return true if the enchant can be applied to this item
     */
    boolean canApplyEnchant(@NotNull ItemStack item, @NotNull String key);

    /**
     * Applies all potion enchants from the player's equipped armor.
     * This forces a re-application of all potion effects based on the player's current armor.
     * @param player the player to apply potion enchants to
     */
    void applyAllPotionEnchants(@NotNull Player player);
}
