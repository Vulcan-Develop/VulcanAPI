package net.vulcandev.vulcanapi.vulcanenchants.wrapper;

import net.vulcandev.vulcanenchants.enchants.PotionEnchants;
import net.vulcandev.vulcanenchants.interfaces.Enchants;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

/**
 * Wrapper class for Enchants interface to provide safe access to enchant data.
 * This class exposes enchant information to external plugins without giving direct access
 * to internal VulcanEnchants objects.
 */
public class EnchantWrapper {
    private final Enchants enchant;
    private final boolean isPotionEnchant;

    public EnchantWrapper(@NotNull Enchants enchant) {
        this.enchant = enchant;
        this.isPotionEnchant = enchant instanceof PotionEnchants;
    }

    /**
     * Gets the enchant's unique key/identifier.
     *
     * @return the enchant key
     */
    @NotNull
    public String getKey() {
        return enchant.getKey();
    }

    /**
     * Gets the XP cost to purchase this enchant.
     *
     * @return the XP cost
     */
    public int getXpCost() {
        return enchant.getXpCost();
    }

    /**
     * Checks if the enchant is enabled in the config.
     *
     * @return true if enabled in config
     */
    public boolean isConfigEnabled() {
        return enchant.isConfigEnabled();
    }

    /**
     * Checks if the enchant is currently enabled.
     *
     * @return true if enabled
     */
    public boolean isEnabled() {
        return enchant.isEnabled();
    }

    /**
     * Gets the set of materials this enchant can be applied to.
     *
     * @return unmodifiable set of applicable materials
     */
    @NotNull
    public Set<Material> getApplicableMaterials() {
        return Collections.unmodifiableSet(enchant.getArmorMaterials());
    }

    /**
     * Gets the lore text that appears on armor with this enchant.
     *
     * @return the armor lore text
     */
    @Nullable
    public String getArmorLore() {
        return enchant.getArmorLore();
    }

    /**
     * Gets the enchant book item.
     *
     * @return a clone of the book ItemStack
     */
    @NotNull
    public ItemStack getBookItem() {
        return enchant.getBookItem().clone();
    }

    /**
     * Checks if this enchant is a potion enchant.
     *
     * @return true if this is a potion enchant
     */
    public boolean isPotionEnchant() {
        return isPotionEnchant;
    }

    /**
     * Gets the potion effect type if this is a potion enchant.
     *
     * @return the PotionEffectType, or null if not a potion enchant
     */
    @Nullable
    public PotionEffectType getPotionEffectType() {
        if (isPotionEnchant) {
            return ((PotionEnchants) enchant).getEffect();
        }
        return null;
    }

    /**
     * Gets the potion amplifier if this is a potion enchant.
     *
     * @return the amplifier level, or -1 if not a potion enchant
     */
    public int getPotionAmplifier() {
        if (isPotionEnchant) {
            return ((PotionEnchants) enchant).getAmp();
        }
        return -1;
    }

    /**
     * Gets the type of enchant.
     *
     * @return "POTION" for potion enchants, "CUSTOM" for custom enchants
     */
    @NotNull
    public String getEnchantType() {
        return isPotionEnchant ? "POTION" : "CUSTOM";
    }

    /**
     * Checks if this enchant can be applied to a specific material.
     *
     * @param material the material to check
     * @return true if the enchant can be applied to this material
     */
    public boolean canApplyTo(@NotNull Material material) {
        return enchant.getArmorMaterials().contains(material);
    }

    @Override
    public String toString() {
        return "EnchantWrapper{" +
                "key='" + getKey() + '\'' +
                ", type=" + getEnchantType() +
                ", enabled=" + isEnabled() +
                ", xpCost=" + getXpCost() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnchantWrapper that = (EnchantWrapper) o;
        return getKey().equals(that.getKey());
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }
}
