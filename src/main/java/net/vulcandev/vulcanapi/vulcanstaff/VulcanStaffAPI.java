package net.vulcandev.vulcanapi.vulcanstaff;

import lombok.Getter;
import net.vulcandev.vulcanapi.interfaces.staff.IVulcanStaffPlugin;
import net.vulcandev.vulcanapi.vulcanstaff.events.PlayerFreezeEvent;
import net.vulcandev.vulcanapi.vulcanstaff.events.StaffVanishEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class VulcanStaffAPI {
    private static VulcanStaffAPI instance;

    @Getter
    private final IVulcanStaffPlugin plugin;

    public VulcanStaffAPI(IVulcanStaffPlugin plugin) {
        this.plugin = plugin;
    }

    public static VulcanStaffAPI getInstance() {
        return instance;
    }

    public static boolean isAvailable() {
        return instance != null && instance.plugin != null;
    }

    public boolean isVanished(Player player) {
        return isVanished(player.getUniqueId());
    }

    public boolean isVanished(UUID uuid) {
        return isAvailable() && plugin.isVanished(uuid);
    }

    public boolean setVanished(Player player, boolean vanished) {
        if (!isAvailable()) return false;

        StaffVanishEvent event = new StaffVanishEvent(player, vanished, StaffVanishEvent.VanishReason.API);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;

        plugin.setVanished(player.getUniqueId(), vanished);
        return true;
    }

    public boolean canSeeVanished(Player player) {
        return isAvailable() && plugin.canSeeVanished(player.getUniqueId());
    }

    public Set<UUID> getVanishedPlayers() {
        return isAvailable() ? plugin.getVanishedPlayers() : Collections.emptySet();
    }

    public boolean isInStaffMode(Player player) {
        return isInStaffMode(player.getUniqueId());
    }

    public boolean isInStaffMode(UUID uuid) {
        return isAvailable() && plugin.isInStaffMode(uuid);
    }

    public boolean isFrozen(Player player) {
        return isFrozen(player.getUniqueId());
    }

    public boolean isFrozen(UUID uuid) {
        return isAvailable() && plugin.isFrozen(uuid);
    }

    public boolean setFrozen(Player target, Player staff, boolean frozen) {
        if (!isAvailable()) return false;

        PlayerFreezeEvent event = new PlayerFreezeEvent(target, staff, frozen, PlayerFreezeEvent.FreezeReason.API);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;

        plugin.setFrozen(target.getUniqueId(), frozen);
        return true;
    }

    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin instanceof IVulcanStaffPlugin) {
            instance = new VulcanStaffAPI((IVulcanStaffPlugin) plugin);
        }
    }

    public static void cleanup() {
        instance = null;
    }
}
