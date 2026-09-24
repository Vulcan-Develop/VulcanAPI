package net.vulcandev.vulcanapi.vulcanstats;

import lombok.Getter;
import net.vulcandev.vulcanstats.VulcanStats;
import org.jetbrains.annotations.ApiStatus;

import java.util.UUID;

public class VulcanStatsAPI {
    private static VulcanStatsAPI instance;

    @Getter
    @ApiStatus.Internal
    private final VulcanStats plugin;

    @ApiStatus.Internal
    public VulcanStatsAPI(VulcanStats plugin) {
        this.plugin = plugin;
    }

    public static VulcanStatsAPI getInstance() {
        return instance;
    }

    public static boolean isAvailable() {
        return instance != null && instance.plugin != null && instance.plugin.isEnabled();
    }

    public Object getPlayerStats(UUID uuid) {
        return plugin.getStatsManager().getPlayerStats(uuid);
    }

    @ApiStatus.Internal
    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin.getClass().getName().equals("net.vulcandev.vulcanstats.VulcanStats")) {
            instance = new VulcanStatsAPI((VulcanStats) plugin);
        }
    }

    @ApiStatus.Internal
    public static void cleanup() {
        instance = null;
    }
}
