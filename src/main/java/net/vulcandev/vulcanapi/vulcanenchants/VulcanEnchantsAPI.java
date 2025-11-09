package net.vulcandev.vulcanapi.vulcanenchants;

import net.vulcandev.vulcanapi.interfaces.enchants.IEnchantWrapper;
import net.vulcandev.vulcanapi.interfaces.enchants.IVulcanEnchantsPlugin;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Main API class for VulcanEnchants.
 * This class provides a clean interface for external plugins to interact with VulcanEnchants.
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * VulcanEnchantsAPI api = VulcanEnchantsAPI.getInstance();
 * if (api != null && VulcanEnchantsAPI.isAvailable()) {
 *     IEnchantWrapper enchant = api.getEnchant("warrior");
 *     // Use the enchant...
 * }
 * }</pre>
 */
public class VulcanEnchantsAPI {
    private static VulcanEnchantsAPI instance;
    private final IVulcanEnchantsPlugin plugin;

    public VulcanEnchantsAPI(IVulcanEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets the API instance.
     *
     * @return the VulcanEnchantsAPI instance, or null if VulcanEnchants is not loaded
     */
    public static VulcanEnchantsAPI getInstance() {
        return instance;
    }

    /**
     * Checks if VulcanEnchants is available and loaded.
     *
     * @return true if VulcanEnchants is available, false otherwise
     */
    public static boolean isAvailable() {
        return instance != null && instance.plugin != null;
    }

    // ===== ENCHANT MANAGER API =====

    /**
     * Gets an enchant by its key/name.
     *
     * @param key the enchant key
     * @return IEnchantWrapper containing enchant information, or null if not found
     */
    @Nullable
    public IEnchantWrapper getEnchant(@NotNull String key) {
        if (!isAvailable()) return null;
        return plugin.getEnchant(key);
    }

    /**
     * Gets all registered enchants.
     *
     * @return Set of all enchant keys
     */
    @NotNull
    public Set<String> getAllEnchantKeys() {
        if (!isAvailable()) return Collections.emptySet();
        return plugin.getAllEnchantKeys();
    }

    /**
     * Gets all registered enchants as wrappers.
     *
     * @return List of IEnchantWrappers
     */
    @NotNull
    public List<IEnchantWrapper> getAllEnchants() {
        if (!isAvailable()) return Collections.emptyList();
        return plugin.getAllEnchants();
    }

    /**
     * Gets all potion enchants.
     *
     * @return List of potion enchant wrappers
     */
    @NotNull
    public List<IEnchantWrapper> getPotionEnchants() {
        if (!isAvailable()) return Collections.emptyList();
        return plugin.getPotionEnchants();
    }

    /**
     * Gets all custom/extra enchants.
     *
     * @return List of custom enchant wrappers
     */
    @NotNull
    public List<IEnchantWrapper> getCustomEnchants() {
        if (!isAvailable()) return Collections.emptyList();
        return plugin.getCustomEnchants();
    }

    /**
     * Checks if an enchant exists.
     *
     * @param key the enchant key
     * @return true if the enchant exists
     */
    public boolean enchantExists(@NotNull String key) {
        if (!isAvailable()) return false;
        return plugin.enchantExists(key);
    }

    /**
     * Checks if an enchant is enabled.
     *
     * @param key the enchant key
     * @return true if the enchant is enabled
     */
    public boolean isEnchantEnabled(@NotNull String key) {
        if (!isAvailable()) return false;
        return plugin.isEnchantEnabled(key);
    }

    /**
     * Gets the XP cost of an enchant.
     *
     * @param key the enchant key
     * @return the XP cost, or -1 if enchant not found
     */
    public int getEnchantCost(@NotNull String key) {
        if (!isAvailable()) return -1;
        return plugin.getEnchantCost(key);
    }

    /**
     * Gets the book item for an enchant.
     *
     * @param key the enchant key
     * @return the enchant book ItemStack, or null if not found
     */
    @Nullable
    public ItemStack getEnchantBook(@NotNull String key) {
        if (!isAvailable()) return null;
        return plugin.getEnchantBook(key);
    }

    // ===== POTION ENCHANT SPECIFIC API =====

    /**
     * Checks if an enchant is a potion enchant.
     *
     * @param key the enchant key
     * @return true if it's a potion enchant
     */
    public boolean isPotionEnchant(@NotNull String key) {
        if (!isAvailable()) return false;
        return plugin.isPotionEnchant(key);
    }

    /**
     * Gets the potion effect type name for a potion enchant.
     *
     * @param key the enchant key
     * @return the potion effect type name, or null if not a potion enchant
     */
    @Nullable
    public String getPotionEffectType(@NotNull String key) {
        if (!isAvailable()) return null;
        return plugin.getPotionEffectType(key);
    }

    /**
     * Gets the amplifier level for a potion enchant.
     *
     * @param key the enchant key
     * @return the amplifier level, or -1 if not found
     */
    public int getPotionAmplifier(@NotNull String key) {
        if (!isAvailable()) return -1;
        return plugin.getPotionAmplifier(key);
    }

    // ===== ITEM CHECKING API =====

    /**
     * Checks if an item has a specific enchant applied to it.
     * This checks the item's lore for the enchant.
     *
     * @param item the item to check
     * @param key the enchant key
     * @return true if the item has the enchant
     */
    public boolean itemHasEnchant(@NotNull ItemStack item, @NotNull String key) {
        if (!isAvailable()) return false;
        return plugin.itemHasEnchant(item, key);
    }

    /**
     * Gets all enchants applied to an item.
     *
     * @param item the item to check
     * @return Set of enchant keys that are on this item
     */
    @NotNull
    public Set<String> getItemEnchants(@NotNull ItemStack item) {
        if (!isAvailable()) return Collections.emptySet();
        return plugin.getItemEnchants(item);
    }

    /**
     * Checks if an item can have an enchant applied to it.
     * This checks if the item's material is in the enchant's applicable materials.
     *
     * @param item the item to check
     * @param key the enchant key
     * @return true if the enchant can be applied to this item
     */
    public boolean canApplyEnchant(@NotNull ItemStack item, @NotNull String key) {
        if (!isAvailable()) return false;
        return plugin.canApplyEnchant(item, key);
    }

    /**
     * Initialize the API instance.
     * This should only be called by VulcanEnchants internally.
     *
     * @param plugin the VulcanEnchants plugin instance
     */
    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin instanceof IVulcanEnchantsPlugin) {
            IVulcanEnchantsPlugin vulcanEnchants = (IVulcanEnchantsPlugin) plugin;
            instance = new VulcanEnchantsAPI(vulcanEnchants);
        }
    }

    /**
     * Clean up the API instance.
     * This should only be called by VulcanEnchants internally.
     */
    public static void cleanup() {
        instance = null;
    }
}
