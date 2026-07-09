package net.vulcandev.vulcanapi.vulcanenchants;

import lombok.Getter;
import net.vulcandev.vulcanapi.interfaces.enchants.IEnchantWrapper;
import net.vulcandev.vulcanapi.interfaces.enchants.IVulcanEnchantsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class VulcanEnchantsAPI {
    private static VulcanEnchantsAPI instance;

    @Getter
    private final IVulcanEnchantsPlugin plugin;

    public VulcanEnchantsAPI(IVulcanEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    public static VulcanEnchantsAPI getInstance() {
        return instance;
    }

    public static boolean isAvailable() {
        return instance != null && instance.plugin != null;
    }

    @Nullable
    public IEnchantWrapper getEnchant(@NotNull String key) {
        if (!isAvailable()) return null;
        return plugin.getEnchant(key);
    }

    @NotNull
    public Set<String> getAllEnchantKeys() {
        if (!isAvailable()) return Collections.emptySet();
        return plugin.getAllEnchantKeys();
    }

    @NotNull
    public List<IEnchantWrapper> getAllEnchants() {
        if (!isAvailable()) return Collections.emptyList();
        return plugin.getAllEnchants();
    }

    @NotNull
    public List<IEnchantWrapper> getPotionEnchants() {
        if (!isAvailable()) return Collections.emptyList();
        return plugin.getPotionEnchants();
    }

    @NotNull
    public List<IEnchantWrapper> getCustomEnchants() {
        if (!isAvailable()) return Collections.emptyList();
        return plugin.getCustomEnchants();
    }

    public boolean enchantExists(@NotNull String key) {
        if (!isAvailable()) return false;
        return plugin.enchantExists(key);
    }

    public boolean isEnchantEnabled(@NotNull String key) {
        if (!isAvailable()) return false;
        return plugin.isEnchantEnabled(key);
    }

    public int getEnchantCost(@NotNull String key) {
        if (!isAvailable()) return -1;
        return plugin.getEnchantCost(key);
    }

    @Nullable
    public ItemStack getEnchantBook(@NotNull String key) {
        if (!isAvailable()) return null;
        return plugin.getEnchantBook(key);
    }

    public boolean isPotionEnchant(@NotNull String key) {
        if (!isAvailable()) return false;
        return plugin.isPotionEnchant(key);
    }

    @Nullable
    public String getPotionEffectType(@NotNull String key) {
        if (!isAvailable()) return null;
        return plugin.getPotionEffectType(key);
    }

    public int getPotionAmplifier(@NotNull String key) {
        if (!isAvailable()) return -1;
        return plugin.getPotionAmplifier(key);
    }

    public boolean itemHasEnchant(@NotNull ItemStack item, @NotNull String key) {
        if (!isAvailable()) return false;
        return plugin.itemHasEnchant(item, key);
    }

    @NotNull
    public Set<String> getItemEnchants(@NotNull ItemStack item) {
        if (!isAvailable()) return Collections.emptySet();
        return plugin.getItemEnchants(item);
    }

    public boolean canApplyEnchant(@NotNull ItemStack item, @NotNull String key) {
        if (!isAvailable()) return false;
        return plugin.canApplyEnchant(item, key);
    }

    public void applyAllPotionEnchants(@NotNull Player player) {
        if (!isAvailable()) return;
        plugin.applyAllPotionEnchants(player);
    }

    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin instanceof IVulcanEnchantsPlugin) {
            instance = new VulcanEnchantsAPI((IVulcanEnchantsPlugin) plugin);
        }
    }

    public static void cleanup() {
        instance = null;
    }
}
