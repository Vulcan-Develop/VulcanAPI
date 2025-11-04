package net.vulcandev.vulcanapi;

import net.vulcandev.vulcanapi.vulcanevents.VulcanEventsAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class VulcanAPI extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Register listener for plugin enable/disable events
        Bukkit.getPluginManager().registerEvents(this, this);

        // Check if VulcanEvents is already loaded (in case of reload)
        Plugin vulcanEvents = Bukkit.getPluginManager().getPlugin("VulcanEvents");
        if (vulcanEvents != null && vulcanEvents.isEnabled()) {
            getLogger().info("Detected VulcanEvents already loaded - injecting VulcanEventsAPI");
            VulcanEventsAPI.initialize(vulcanEvents);
        }
    }

    @Override
    public void onDisable() {
        // Cleanup VulcanEventsAPI
        VulcanEventsAPI.cleanup();
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        Plugin plugin = event.getPlugin();

        // Auto-inject VulcanEventsAPI when VulcanEvents enables
        if (plugin.getName().equals("VulcanEvents")) {
            getLogger().info("VulcanEvents enabled - injecting VulcanEventsAPI");
            VulcanEventsAPI.initialize(plugin);
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        Plugin plugin = event.getPlugin();

        // Cleanup VulcanEventsAPI when VulcanEvents disables
        if (plugin.getName().equals("VulcanEvents")) {
            getLogger().info("VulcanEvents disabled - cleaning up VulcanEventsAPI");
            VulcanEventsAPI.cleanup();
        }
    }
}
