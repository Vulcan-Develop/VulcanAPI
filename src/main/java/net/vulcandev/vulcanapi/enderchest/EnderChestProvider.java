package net.vulcandev.vulcanapi.enderchest;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Optional server-wide source of custom ender chest contents.
 *
 * <p>Providers register this interface with Bukkit's services manager. Consumers must resolve it
 * lazily and retain their native ender chest fallback so servers without a provider are unchanged.</p>
 */
public interface EnderChestProvider {

    /** Returns a defensive snapshot of every stored ender chest slot for an online player. */
    @NotNull
    ItemStack[] getEnderChestContents(@NotNull Player player);

    /**
     * Restores the supplied slots for an online player.
     *
     * <p>A provider may preserve slots beyond {@code contents.length}; this allows legacy 27-slot
     * saves to be restored without deleting newer expansion slots.</p>
     *
     * @return true when the restore was applied and persisted
     */
    boolean restoreEnderChestContents(@NotNull Player player, @NotNull ItemStack[] contents);

    /** Resolves the currently registered provider, or {@code null} when vanilla storage is active. */
    static EnderChestProvider get() {
        RegisteredServiceProvider<EnderChestProvider> registration =
                Bukkit.getServicesManager().getRegistration(EnderChestProvider.class);
        return registration == null ? null : registration.getProvider();
    }
}
