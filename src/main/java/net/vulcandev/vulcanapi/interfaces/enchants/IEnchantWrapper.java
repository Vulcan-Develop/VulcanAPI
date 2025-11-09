package net.vulcandev.vulcanapi.interfaces.enchants;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Interface for enchant wrapper to avoid direct class dependencies
 * This allows VulcanAPI to work with enchants across classloader boundaries
 */
public interface IEnchantWrapper {

    /**
     * Gets the enchant key/identifier
     * @return the enchant key
     */
    @NotNull
    String getKey();

    /**
     * Gets the display name of the enchant
     * @return the display name
     */
    @NotNull
    String getDisplayName();

    /**
     * Gets the armor lore for this enchant
     * @return the armor lore string
     */
    @Nullable
    String getArmorLore();

    /**
     * Gets the XP cost for this enchant
     * @return the XP cost
     */
    int getXpCost();

    /**
     * Gets the book item for this enchant
     * @return the enchant book ItemStack
     */
    @NotNull
    ItemStack getBookItem();

    /**
     * Gets the applicable armor materials for this enchant
     * @return list of applicable materials
     */
    @NotNull
    List<Material> getArmorMaterials();

    /**
     * Checks if this enchant is enabled
     * @return true if enabled
     */
    boolean isEnabled();

    /**
     * Checks if this enchant is enabled in config
     * @return true if config enabled
     */
    boolean isConfigEnabled();
}
