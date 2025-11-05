package net.vulcandev.vulcanapi.wrapper;

import org.bukkit.plugin.Plugin;
import java.util.function.Consumer;

public class PluginIntegration {
    private final String pluginName;
    private final Consumer<Plugin> initializer;
    private final Runnable cleanup;

    public PluginIntegration(String pluginName, Consumer<Plugin> initializer, Runnable cleanup) {
        this.pluginName = pluginName;
        this.initializer = initializer;
        this.cleanup = cleanup;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void initialize(Plugin plugin) {
        initializer.accept(plugin);
    }

    public void cleanup() {
        cleanup.run();
    }
}
