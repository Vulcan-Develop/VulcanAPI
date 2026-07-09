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

public class EnchantWrapper {
    private final Enchants enchant;
    private final boolean isPotionEnchant;

    public EnchantWrapper(@NotNull Enchants enchant) {
        this.enchant = enchant;
        this.isPotionEnchant = enchant instanceof PotionEnchants;
    }

    @NotNull
    public String getKey() {
        return enchant.getKey();
    }

    public int getXpCost() {
        return enchant.getXpCost();
    }

    public boolean isConfigEnabled() {
        return enchant.isConfigEnabled();
    }

    public boolean isEnabled() {
        return enchant.isEnabled();
    }

    @NotNull
    public Set<Material> getApplicableMaterials() {
        return Collections.unmodifiableSet(enchant.getArmorMaterials());
    }

    @Nullable
    public String getArmorLore() {
        return enchant.getArmorLore();
    }

    @NotNull
    public ItemStack getBookItem() {
        return enchant.getBookItem().clone();
    }

    public boolean isPotionEnchant() {
        return isPotionEnchant;
    }

    @Nullable
    public PotionEffectType getPotionEffectType() {
        if (isPotionEnchant) {
            return ((PotionEnchants) enchant).getEffect();
        }
        return null;
    }

    public int getPotionAmplifier() {
        if (isPotionEnchant) {
            return ((PotionEnchants) enchant).getAmp();
        }
        return -1;
    }

    @NotNull
    public String getEnchantType() {
        return isPotionEnchant ? "POTION" : "CUSTOM";
    }

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
