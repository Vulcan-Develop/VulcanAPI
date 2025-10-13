package net.vulcandev.vulcanapi.vulcanenchants;

import net.vulcandev.vulcanapi.vulcanenchants.wrapper.EnchantWrapper;
import net.vulcandev.vulcanenchants.VulcanEnchants;
import net.vulcandev.vulcanenchants.enchants.ExtraEnchants;
import net.vulcandev.vulcanenchants.enchants.PotionEnchants;
import net.vulcandev.vulcanenchants.interfaces.Enchants;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Main API class for VulcanEnchants.
 * This class provides a clean interface for external plugins to interact with VulcanEnchants.
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * VulcanEnchantsAPI api = VulcanEnchantsAPI.getInstance();
 * if (api != null && VulcanEnchantsAPI.isAvailable()) {
 *     EnchantWrapper enchant = api.getEnchant("warrior");
 *     // Use the enchant...
 * }
 * }</pre>
 */
public class VulcanEnchantsAPI {
    private static VulcanEnchantsAPI instance;
    private final VulcanEnchants plugin;

    public VulcanEnchantsAPI(VulcanEnchants plugin) {
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
        return instance != null && instance.plugin != null && instance.plugin.isEnabled();
    }

    // ===== ENCHANT MANAGER API =====

    /**
     * Gets an enchant by its key/name.
     *
     * @param key the enchant key
     * @return EnchantWrapper containing enchant information, or null if not found
     */
    @Nullable
    public EnchantWrapper getEnchant(@NotNull String key) {
        if (!isAvailable()) return null;

        Enchants enchant = plugin.getEnchantManager().getEnchants().get(key);
        if (enchant == null) return null;

        return new EnchantWrapper(enchant);
    }

    /**
     * Gets all registered enchants.
     *
     * @return Set of all enchant keys
     */
    @NotNull
    public Set<String> getAllEnchantKeys() {
        if (!isAvailable()) return Collections.emptySet();
        return new HashSet<>(plugin.getEnchantManager().getEnchants().keySet());
    }

    /**
     * Gets all registered enchants as wrappers.
     *
     * @return List of EnchantWrappers
     */
    @NotNull
    public List<EnchantWrapper> getAllEnchants() {
        if (!isAvailable()) return Collections.emptyList();

        return plugin.getEnchantManager().getEnchants().values().stream()
                .map(EnchantWrapper::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets all potion enchants.
     *
     * @return List of potion enchant wrappers
     */
    @NotNull
    public List<EnchantWrapper> getPotionEnchants() {
        if (!isAvailable()) return Collections.emptyList();

        return plugin.getEnchantManager().getPotionEnchants().values().stream()
                .map(EnchantWrapper::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets all custom/extra enchants.
     *
     * @return List of custom enchant wrappers
     */
    @NotNull
    public List<EnchantWrapper> getCustomEnchants() {
        if (!isAvailable()) return Collections.emptyList();

        return plugin.getEnchantManager().getExtraEnchants().values().stream()
                .map(EnchantWrapper::new)
                .collect(Collectors.toList());
    }

    /**
     * Checks if an enchant exists.
     *
     * @param key the enchant key
     * @return true if the enchant exists
     */
    public boolean enchantExists(@NotNull String key) {
        if (!isAvailable()) return false;
        return plugin.getEnchantManager().getEnchants().containsKey(key);
    }

    /**
     * Checks if an enchant is enabled.
     *
     * @param key the enchant key
     * @return true if the enchant is enabled
     */
    public boolean isEnchantEnabled(@NotNull String key) {
        if (!isAvailable()) return false;

        Enchants enchant = plugin.getEnchantManager().getEnchants().get(key);
        return enchant != null && enchant.isEnabled() && enchant.isConfigEnabled();
    }

    /**
     * Gets the XP cost of an enchant.
     *
     * @param key the enchant key
     * @return the XP cost, or -1 if enchant not found
     */
    public int getEnchantCost(@NotNull String key) {
        if (!isAvailable()) return -1;

        Enchants enchant = plugin.getEnchantManager().getEnchants().get(key);
        return enchant != null ? enchant.getXpCost() : -1;
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

        Enchants enchant = plugin.getEnchantManager().getEnchants().get(key);
        return enchant != null ? enchant.getBookItem() : null;
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
        return plugin.getEnchantManager().getPotionEnchants().containsKey(key);
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

        PotionEnchants enchant = plugin.getEnchantManager().getPotionEnchants().get(key);
        return enchant != null && enchant.getEffect() != null ? enchant.getEffect().getName() : null;
    }

    /**
     * Gets the amplifier level for a potion enchant.
     *
     * @param key the enchant key
     * @return the amplifier level, or -1 if not found
     */
    public int getPotionAmplifier(@NotNull String key) {
        if (!isAvailable()) return -1;

        PotionEnchants enchant = plugin.getEnchantManager().getPotionEnchants().get(key);
        return enchant != null ? enchant.getAmp() : -1;
    }

    /**
     * Manually applies a potion enchant to a player from an item.
     * This will check if the item has the enchant lore and apply the effect.
     *
     * @param player the player to apply the effect to
     * @param key the enchant key
     * @param item the item with the enchant
     * @return true if successfully applied
     */
    public boolean applyPotionEnchant(@NotNull Player player, @NotNull String key, @NotNull ItemStack item) {
        if (!isAvailable()) return false;

        PotionEnchants enchant = plugin.getEnchantManager().getPotionEnchants().get(key);
        if (enchant == null) return false;

        enchant.applyPotion(player, item);
        return true;
    }

    /**
     * Manually removes a potion enchant effect from a player.
     *
     * @param player the player to remove the effect from
     * @param key the enchant key
     * @param item the item with the enchant
     * @return true if successfully removed
     */
    public boolean removePotionEnchant(@NotNull Player player, @NotNull String key, @NotNull ItemStack item) {
        if (!isAvailable()) return false;

        PotionEnchants enchant = plugin.getEnchantManager().getPotionEnchants().get(key);
        if (enchant == null) return false;

        enchant.removePotion(player, item);
        return true;
    }

    /**
     * Applies all potion enchants from a player's currently equipped armor.
     * This scans all armor pieces and applies any potion enchants found.
     *
     * @param player the player to apply enchants to
     * @return the number of enchants successfully applied
     */
    public int applyAllPotionEnchants(@NotNull Player player) {
        if (!isAvailable()) return 0;

        int appliedCount = 0;
        ItemStack[] armorContents = player.getInventory().getArmorContents();

        for (ItemStack armorPiece : armorContents) {
            if (armorPiece == null || armorPiece.getType() == org.bukkit.Material.AIR) continue;

            for (PotionEnchants enchant : plugin.getEnchantManager().getPotionEnchants().values()) {
                if (enchant.isConfigEnabled() && enchant.isEnabled()) {
                    enchant.applyPotion(player, armorPiece);
                    appliedCount++;
                }
            }
        }

        return appliedCount;
    }

    /**
     * Removes all potion enchant effects from a player.
     * This removes all potion effects that are associated with enchants,
     * checking the player's current armor.
     *
     * @param player the player to remove enchants from
     * @return the number of enchants successfully removed
     */
    public int removeAllPotionEnchants(@NotNull Player player) {
        if (!isAvailable()) return 0;

        int removedCount = 0;
        ItemStack[] armorContents = player.getInventory().getArmorContents();

        for (ItemStack armorPiece : armorContents) {
            if (armorPiece == null || armorPiece.getType() == org.bukkit.Material.AIR) continue;

            for (PotionEnchants enchant : plugin.getEnchantManager().getPotionEnchants().values()) {
                if (enchant.isConfigEnabled() && enchant.isEnabled()) {
                    enchant.removePotion(player, armorPiece);
                    removedCount++;
                }
            }
        }

        return removedCount;
    }

    /**
     * Refreshes all potion enchants for a player by removing and reapplying them.
     * This is useful when armor changes or when you need to update enchant effects.
     *
     * @param player the player to refresh enchants for
     * @return the number of enchants refreshed
     */
    public int refreshAllPotionEnchants(@NotNull Player player) {
        if (!isAvailable()) return 0;

        removeAllPotionEnchants(player);
        return applyAllPotionEnchants(player);
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
        if (!isAvailable() || item == null || !item.hasItemMeta()) return false;

        Enchants enchant = plugin.getEnchantManager().getEnchants().get(key);
        if (enchant == null || enchant.getArmorLore() == null) return false;

        if (!item.getItemMeta().hasLore()) return false;

        String armorLore = enchant.getArmorLore();
        // Apply color codes
        armorLore = armorLore.replace("&", "§");

        return item.getItemMeta().getLore().contains(armorLore);
    }

    /**
     * Gets all enchants applied to an item.
     *
     * @param item the item to check
     * @return Set of enchant keys that are on this item
     */
    @NotNull
    public Set<String> getItemEnchants(@NotNull ItemStack item) {
        if (!isAvailable() || item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) {
            return Collections.emptySet();
        }

        Set<String> enchants = new HashSet<>();
        List<String> lore = item.getItemMeta().getLore();

        for (Map.Entry<String, Enchants> entry : plugin.getEnchantManager().getEnchants().entrySet()) {
            String enchantLore = entry.getValue().getArmorLore();
            if (enchantLore != null) {
                enchantLore = enchantLore.replace("&", "§");
                if (lore.contains(enchantLore)) {
                    enchants.add(entry.getKey());
                }
            }
        }

        return enchants;
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
        if (!isAvailable() || item == null) return false;

        Enchants enchant = plugin.getEnchantManager().getEnchants().get(key);
        if (enchant == null) return false;

        return enchant.getArmorMaterials().contains(item.getType());
    }

    /**
     * Initialize the API instance.
     * This should only be called by VulcanEnchants internally.
     *
     * @param plugin the VulcanEnchants plugin instance
     */
    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin.getClass().getName().equals("net.vulcandev.vulcanenchants.VulcanEnchants")) {
            VulcanEnchants vulcanEnchants = (VulcanEnchants) plugin;
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
