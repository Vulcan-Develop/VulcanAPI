package net.vulcandev.vulcanapi.vulcanvoting;

import lombok.Getter;
import net.vulcandev.vulcanvoting.VulcanVoting;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

public class VulcanVotingAPI {
    private static VulcanVotingAPI instance;

    @Getter
    @ApiStatus.Internal
    private final VulcanVoting plugin;

    private VulcanVotingAPI(VulcanVoting plugin) {
        this.plugin = plugin;
    }

    public static VulcanVotingAPI getInstance() {
        return instance;
    }

    public static boolean isAvailable() {
        return instance != null && instance.plugin != null && instance.plugin.isEnabled();
    }

    @ApiStatus.Internal
    public static void initialize(Plugin plugin) {
        cleanup();
        if (plugin instanceof VulcanVoting) {
            instance = new VulcanVotingAPI((VulcanVoting) plugin);
        }
    }

    @ApiStatus.Internal
    public static void cleanup() {
        instance = null;
    }
}
