package net.vulcandev.vulcanapi.vulcanstats;

import net.vulcandev.vulcanstats.VulcanStats;

import java.util.UUID;

public class VulcanStatsAPI {
    private static VulcanStatsAPI instance;
    private final VulcanStats plugin;

    public VulcanStatsAPI(VulcanStats plugin) { this.plugin = plugin; }

    public static VulcanStatsAPI getInstance() { return instance; }


    public static boolean isAvailable() { return instance != null && instance.plugin != null && instance.plugin.isEnabled();}

    public Object getPlayerStats(UUID uuid) {
        return plugin.getStatsManager().getPlayerStats(uuid);
    }

    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin.getClass().getName().equals("net.vulcandev.vulcanstats.VulcanStats")) {
            VulcanStats vulcanStats = (VulcanStats) plugin;
            instance = new VulcanStatsAPI(vulcanStats);
        }
    }

    public static void cleanup() {instance = null;}
}
