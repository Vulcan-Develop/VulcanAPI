package net.vulcandev.vulcanapi.vulcancrates;

import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.event.VulcanEventManager;
import net.vulcandev.vulcanapi.event.VulcanListener;
import org.bukkit.plugin.Plugin;

/**
 * Minimal API bridge for VulcanCrates events.
 */
public final class VulcanCratesAPI {
    private static VulcanCratesAPI instance;
    private final Plugin plugin;

    private VulcanCratesAPI(Plugin plugin) {
        this.plugin = plugin;
    }

    public static VulcanCratesAPI getInstance() {
        return instance;
    }

    public static boolean isAvailable() {
        return instance != null && instance.plugin != null && instance.plugin.isEnabled();
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void registerListener(VulcanListener listener) {
        VulcanEventManager.getInstance().registerListener(listener);
    }

    public void unregisterListener(VulcanListener listener) {
        VulcanEventManager.getInstance().unregisterListener(listener);
    }

    public boolean callEvent(VulcanEvent event) {
        return VulcanEventManager.getInstance().callEvent(event);
    }

    public static void initialize(Plugin plugin) {
        cleanup();
        if (plugin != null && "VulcanCrates".equals(plugin.getName())) {
            instance = new VulcanCratesAPI(plugin);
        }
    }

    public static void cleanup() {
        instance = null;
    }
}
