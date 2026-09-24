package net.vulcandev.vulcanapi.opaque;

import net.vulcandev.vulcanapi.event.VulcanEventManager;
import net.vulcandev.vulcanapi.event.VulcanListener;
import net.vulcandev.vulcanapi.opaque.event.OpaquePlayerVisibilityEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

public final class OpaqueAPI {

    private OpaqueAPI() {
    }

    public static boolean isAvailable() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Opaque");
        return plugin != null && plugin.isEnabled();
    }

    public static void registerListener(VulcanListener listener) {
        VulcanEventManager.getInstance().registerListener(listener);
    }

    public static void unregisterListener(VulcanListener listener) {
        VulcanEventManager.getInstance().unregisterListener(listener);
    }

    @ApiStatus.Internal
    public static void callEvent(OpaquePlayerVisibilityEvent event) {
        VulcanEventManager.getInstance().callEvent(event);
    }
}
