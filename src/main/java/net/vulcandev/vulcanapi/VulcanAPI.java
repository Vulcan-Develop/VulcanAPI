package net.vulcandev.vulcanapi;

import net.vulcandev.vulcanapi.vulcanenchants.VulcanEnchantsAPI;
import net.vulcandev.vulcanapi.vulcanevents.VulcanEventsAPI;
import net.vulcandev.vulcanapi.vulcanstaff.VulcanStaffAPI;
import net.vulcandev.vulcanapi.vulcanstats.VulcanStatsAPI;
import net.vulcandev.vulcanapi.vulcantools.VulcanToolsAPI;
import net.vulcandev.vulcanapi.vulcanvoting.VulcanVotingAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class VulcanAPI extends JavaPlugin implements Listener {

    private static final class ApiIntegration {
        private final String pluginName;
        private final String apiDisplayName;
        private final Consumer<Plugin> initializer;
        private final Runnable cleanup;

        private ApiIntegration(String pluginName, String apiDisplayName,
                               Consumer<Plugin> initializer, Runnable cleanup) {
            this.pluginName = pluginName;
            this.apiDisplayName = apiDisplayName;
            this.initializer = initializer;
            this.cleanup = cleanup;
        }

        private String getPluginName() {
            return pluginName;
        }

        private String getApiDisplayName() {
            return apiDisplayName;
        }

        private Consumer<Plugin> getInitializer() {
            return initializer;
        }

        private Runnable getCleanup() {
            return cleanup;
        }
    }

    private static final Map<String, ApiIntegration> API_INTEGRATIONS = createIntegrationMap();

    private static Map<String, ApiIntegration> createIntegrationMap() {
        Map<String, ApiIntegration> integrations = new HashMap<>();
        integrations.put("VulcanEnchants", new ApiIntegration("VulcanEnchants", "VulcanEnchantsAPI", VulcanEnchantsAPI::initialize, VulcanEnchantsAPI::cleanup));
        integrations.put("VulcanEvents", new ApiIntegration("VulcanEvents", "VulcanEventsAPI", VulcanEventsAPI::initialize, VulcanEventsAPI::cleanup));
        integrations.put("VulcanStaff", new ApiIntegration("VulcanStaff", "VulcanStaffAPI", VulcanStaffAPI::initialize, VulcanStaffAPI::cleanup));
        integrations.put("VulcanStats", new ApiIntegration("VulcanStats", "VulcanStatsAPI", VulcanStatsAPI::initialize, VulcanStatsAPI::cleanup));
        integrations.put("VulcanTools", new ApiIntegration("VulcanTools", "VulcanToolsAPI", VulcanToolsAPI::initialize, VulcanToolsAPI::cleanup));
        integrations.put("VulcanVoting", new ApiIntegration("VulcanVoting", "VulcanVotingAPI", VulcanVotingAPI::initialize, VulcanVotingAPI::cleanup));
        return Collections.unmodifiableMap(integrations);
    }

    @Override
    public void onEnable() {
        // Register listener for plugin enable/disable events
        Bukkit.getPluginManager().registerEvents(this, this);

        // Inject APIs for any already-loaded Vulcan plugins (covers reload scenarios)
        API_INTEGRATIONS.values().forEach(this::hookIfEnabled);
    }

    @Override
    public void onDisable() {
        // Ensure all API singletons are cleaned up if this plugin unloads first
        API_INTEGRATIONS.values().forEach(integration -> integration.getCleanup().run());
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        Plugin plugin = event.getPlugin();

        ApiIntegration integration = API_INTEGRATIONS.get(plugin.getName());
        if (integration != null) {
            integration.getInitializer().accept(plugin);
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        Plugin plugin = event.getPlugin();

        ApiIntegration integration = API_INTEGRATIONS.get(plugin.getName());
        if (integration != null) {
            integration.getCleanup().run();
        }
    }

    private void hookIfEnabled(ApiIntegration integration) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(integration.getPluginName());
        if (plugin != null && plugin.isEnabled()) {
            integration.getInitializer().accept(plugin);
        }
    }

}
