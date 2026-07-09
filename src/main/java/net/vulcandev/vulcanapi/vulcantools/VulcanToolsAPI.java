package net.vulcandev.vulcanapi.vulcantools;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.event.VulcanEventManager;
import net.vulcandev.vulcanapi.event.VulcanListener;
import net.vulcandev.vulcanapi.interfaces.tools.IVulcanToolsPlugin;
import net.vulcandev.vulcanapi.vulcantools.interfaces.IBoosterManager;
import net.vulcandev.vulcanapi.vulcantools.interfaces.ICurrencyManager;
import net.vulcandev.vulcanapi.vulcantools.interfaces.IEventManager;

public class VulcanToolsAPI {
    private static VulcanToolsAPI instance;

    @Getter
    private final IVulcanToolsPlugin plugin;

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

    public boolean callEvent(VulcanEvent event) {
        return VulcanEventManager.getInstance().callEvent(event);
    }

    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin instanceof IVulcanToolsPlugin) {
            instance = new VulcanToolsAPI((IVulcanToolsPlugin) plugin);
        }
    }

    public static void cleanup() {
        instance = null;
    }
}
