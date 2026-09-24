package net.vulcandev.vulcanapi.vulcangift;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.event.VulcanEventManager;
import net.vulcandev.vulcanapi.event.VulcanListener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

public final class VulcanGiftAPI {
    private static VulcanGiftAPI instance;

    @Getter
    @ApiStatus.Internal
    private final Plugin plugin;

    private VulcanGiftAPI(Plugin plugin) {
        this.plugin = plugin;
    }

    public static VulcanGiftAPI getInstance() {
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

    @ApiStatus.Internal
    public boolean callEvent(VulcanEvent event) {
        return VulcanEventManager.getInstance().callEvent(event);
    }

    @ApiStatus.Internal
    public static void initialize(Plugin plugin) {
        cleanup();
        if (plugin != null && "VulcanGifts".equals(plugin.getName())) {
            instance = new VulcanGiftAPI(plugin);
        }
    }

    @ApiStatus.Internal
    public static void cleanup() {
        instance = null;
    }
}
