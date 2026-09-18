package net.vulcandev.vulcanapi.vulcanpass;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.event.VulcanEventManager;
import net.vulcandev.vulcanapi.event.VulcanListener;
import org.bukkit.plugin.Plugin;

public final class VulcanPassAPI {
    private static VulcanPassAPI instance;

    @Getter
    private final Plugin plugin;

    private VulcanPassAPI(Plugin plugin) {
        this.plugin = plugin;
    }

    public static VulcanPassAPI getInstance() {
        return instance;
    }

    public static boolean isAvailable() {
        return instance != null && instance.plugin != null && instance.plugin.isEnabled();
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
        if (plugin != null && "VulcanPass".equals(plugin.getName())) {
            instance = new VulcanPassAPI(plugin);
        }
    }

    public static void cleanup() {
        instance = null;
    }
}
