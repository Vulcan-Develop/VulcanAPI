package net.vulcandev.vulcanapi;

import net.vulcandev.vulcanapi.vulcanenchants.VulcanEnchantsAPI;
import net.vulcandev.vulcanapi.vulcanevents.VulcanEventsAPI;
import net.vulcandev.vulcanapi.vulcanstaff.VulcanStaffAPI;
import net.vulcandev.vulcanapi.vulcanstats.VulcanStatsAPI;
import net.vulcandev.vulcanapi.vulcantools.VulcanToolsAPI;
import net.vulcandev.vulcanapi.vulcanvoting.VulcanVotingAPI;
import net.vulcandev.vulcanapi.wrapper.PluginIntegration;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class VulcanAPI extends JavaPlugin implements Listener {
    private static final PluginIntegration[] INTEGRATIONS = {
        new PluginIntegration("VulcanEnchants", VulcanEnchantsAPI::initialize, VulcanEnchantsAPI::cleanup),
        new PluginIntegration("VulcanEvents",   VulcanEventsAPI::initialize,   VulcanEventsAPI::cleanup),
        new PluginIntegration("VulcanStaff",    VulcanStaffAPI::initialize,    VulcanStaffAPI::cleanup),
        new PluginIntegration("VulcanStats",    VulcanStatsAPI::initialize,    VulcanStatsAPI::cleanup),
        new PluginIntegration("VulcanTools",    VulcanToolsAPI::initialize,    VulcanToolsAPI::cleanup),
        new PluginIntegration("VulcanVoting",   VulcanVotingAPI::initialize,   VulcanVotingAPI::cleanup)
    };

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        for (PluginIntegration integration : INTEGRATIONS) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(integration.getPluginName());
            if (plugin != null && plugin.isEnabled()) {
                try {
                    integration.initialize(plugin);
                    getLogger().info("Hooked into " + integration.getPluginName());
                } catch (Exception e) {
                    getLogger().warning("Failed to hook " + integration.getPluginName() + ": " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onDisable() {
        for (PluginIntegration integration : INTEGRATIONS) {
            try {
                integration.cleanup();
                getLogger().info("Cleaned up " + integration.getPluginName());
            } catch (Exception e) {
                getLogger().warning("Failed to clean up " + integration.getPluginName() + ": " + e.getMessage());
            }
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        String disabledName = event.getPlugin().getName();

        for (PluginIntegration integration : INTEGRATIONS) {
            if (integration.getPluginName().equalsIgnoreCase(disabledName)) {
                try {
                    integration.cleanup();
                    getLogger().info("Cleaned up after " + disabledName + " was disabled.");
                } catch (Exception e) {
                    getLogger().warning("Error cleaning up " + disabledName + ": " + e.getMessage());
                }
                break;
            }
        }
    }
}