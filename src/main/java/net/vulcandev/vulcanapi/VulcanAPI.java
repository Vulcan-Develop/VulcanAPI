package net.vulcandev.vulcanapi;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEventManager;
import net.vulcandev.vulcanloader.loader.VulcanPlugin;
import net.xantharddev.vulcanlib.Logger;

public final class VulcanAPI extends VulcanPlugin {
    @Getter
    private static VulcanAPI instance;

    public void onSecureEnable() {
        instance = this;

        Logger.log("&aVulcanAPI v" + getDescription().getVersion() + " enabled");
        Logger.log("&7Global event system initialized");
        Logger.log("&7Event manager ready for cross-plugin communication");
    }

    public void onSecureDisable() {
        Logger.log("&cShutting down VulcanAPI...");

        VulcanEventManager eventManager = VulcanEventManager.getInstance();
        int listenerCount = eventManager.getTotalListenerCount();

        if (listenerCount > 0) {
            Logger.log("&7Unregistering " + listenerCount + " event listeners...");
        }

        eventManager.shutdown();

        Logger.log("&cVulcanAPI disabled");
        instance = null;
    }
}
