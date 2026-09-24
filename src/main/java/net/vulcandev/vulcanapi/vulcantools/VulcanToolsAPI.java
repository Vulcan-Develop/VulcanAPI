package net.vulcandev.vulcanapi.vulcantools;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.event.VulcanEventManager;
import net.vulcandev.vulcanapi.event.VulcanListener;
import net.vulcandev.vulcanapi.interfaces.tools.IVulcanToolsPlugin;
import net.vulcandev.vulcanapi.vulcantools.interfaces.IBoosterManager;
import net.vulcandev.vulcanapi.vulcantools.interfaces.ICurrencyManager;
import net.vulcandev.vulcanapi.vulcantools.interfaces.IEventManager;
import org.jetbrains.annotations.ApiStatus;

public class VulcanToolsAPI {
    private static VulcanToolsAPI instance;

    @Getter
    @ApiStatus.Internal
    private final IVulcanToolsPlugin plugin;

    @ApiStatus.Internal
    public VulcanToolsAPI(IVulcanToolsPlugin plugin) {
        this.plugin = plugin;
    }

    public static VulcanToolsAPI getInstance() {
        return instance;
    }

    public static boolean isAvailable() {
        return instance != null && instance.plugin != null;
    }

    public ICurrencyManager getCurrencyManager() {
        return plugin.getCurrencyManager();
    }

    public IEventManager getEventManager() {
        return plugin.getEventManager();
    }

    public IBoosterManager getBoosterManager() {
        return plugin.getBoosterManager();
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
    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin instanceof IVulcanToolsPlugin) {
            instance = new VulcanToolsAPI((IVulcanToolsPlugin) plugin);
        }
    }

    @ApiStatus.Internal
    public static void cleanup() {
        instance = null;
    }
}
