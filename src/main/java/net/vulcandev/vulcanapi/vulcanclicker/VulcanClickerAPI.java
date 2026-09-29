package net.vulcandev.vulcanapi.vulcanclicker;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.event.VulcanEventManager;
import net.vulcandev.vulcanapi.event.VulcanListener;
import net.vulcandev.vulcanapi.interfaces.clicker.IVulcanClickerPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Controls VulcanClicker, the hold-to-click auto clicker. Arming and disarming must happen on the
 * server thread; every other method is safe from any thread.
 */
public final class VulcanClickerAPI {
    private static volatile VulcanClickerAPI instance;

    @Getter
    @ApiStatus.Internal
    private final Plugin plugin;

    private final IVulcanClickerPlugin bridge;

    private VulcanClickerAPI(Plugin plugin, IVulcanClickerPlugin bridge) {
        this.plugin = plugin;
        this.bridge = bridge;
    }

    public static VulcanClickerAPI getInstance() {
        return instance;
    }

    public static boolean isAvailable() {
        return instance != null && instance.plugin != null && instance.bridge != null && instance.plugin.isEnabled();
    }

    public void registerListener(VulcanListener listener) {
        VulcanEventManager.getInstance().registerListener(listener);
    }

    public void unregisterListener(VulcanListener listener) {
        VulcanEventManager.getInstance().unregisterListener(listener);
    }

    public boolean isArmed(Player player, ClickerType type) {
        return isArmed(player.getUniqueId(), type);
    }

    public boolean isArmed(UUID uuid, ClickerType type) {
        return isAvailable() && bridge.isArmed(uuid, type);
    }

    /**
     * Arms or disarms a clicker for an online player. Returns false when the change was refused,
     * for example by a cancelled {@link net.vulcandev.vulcanapi.vulcanclicker.events.ClickerArmEvent}.
     */
    public boolean setArmed(Player player, ClickerType type, boolean armed) {
        return isAvailable() && bridge.setArmed(player, type, armed);
    }

    public boolean isClicking(Player player, ClickerType type) {
        return isClicking(player.getUniqueId(), type);
    }

    public boolean isClicking(UUID uuid, ClickerType type) {
        return isAvailable() && bridge.isClicking(uuid, type);
    }

    public double getCps(UUID uuid, ClickerType type) {
        return isAvailable() ? bridge.getCps(uuid, type) : 0.0D;
    }

    @Nullable
    public Double getCpsOverride(UUID uuid, ClickerType type) {
        return isAvailable() ? bridge.getCpsOverride(uuid, type) : null;
    }

    public void setCpsOverride(UUID uuid, ClickerType type, double cps) {
        if (Double.isNaN(cps)) throw new IllegalArgumentException("cps must be a number");
        if (isAvailable()) bridge.setCpsOverride(uuid, type, cps);
    }

    public void clearCpsOverride(UUID uuid, ClickerType type) {
        if (isAvailable()) bridge.setCpsOverride(uuid, type, null);
    }

    public boolean isWhitelisted(@Nullable ItemStack item, ClickerType type) {
        return isAvailable() && bridge.isWhitelisted(item, type);
    }

    public boolean isEnabled(ClickerType type) {
        return isAvailable() && bridge.isClickerEnabled(type);
    }

    public void setClickerEnabled(ClickerType type, boolean enabled) {
        if (isAvailable()) bridge.setClickerEnabled(type, enabled);
    }

    public boolean isBlocked(UUID uuid) {
        return isAvailable() && bridge.isBlocked(uuid);
    }

    /**
     * Blocks or unblocks a player. A blocked player keeps their armed state but never clicks.
     */
    public void setBlocked(UUID uuid, boolean blocked) {
        if (isAvailable()) bridge.setBlocked(uuid, blocked);
    }

    @ApiStatus.Internal
    public boolean callEvent(VulcanEvent event) {
        return VulcanEventManager.getInstance().callEvent(event);
    }

    @ApiStatus.Internal
    public static void initialize(Plugin plugin, IVulcanClickerPlugin bridge) {
        cleanup();
        if (plugin != null && bridge != null && "VulcanClicker".equals(plugin.getName())) {
            instance = new VulcanClickerAPI(plugin, bridge);
        }
    }

    @ApiStatus.Internal
    public static void cleanup() {
        instance = null;
    }
}
