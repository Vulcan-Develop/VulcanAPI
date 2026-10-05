package net.vulcandev.vulcanapi.opaque;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class OpaqueOwnerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent event) {
        if ("Opaque".equals(event.getPlugin().getName())) OpaqueAPI.cleanup();
        OpaqueAPI.release(event.getPlugin());
    }
}
