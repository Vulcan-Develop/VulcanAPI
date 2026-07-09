package net.vulcandev.vulcanapi.vulcanvoting;

import lombok.Getter;
import net.vulcandev.vulcanvoting.VulcanVoting;
import org.bukkit.plugin.Plugin;

public class VulcanVotingAPI {
    private static VulcanVotingAPI instance;

    @Getter
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

    public static void initialize(Plugin plugin) {
        cleanup();
        if (plugin instanceof VulcanVoting) {
            instance = new VulcanVotingAPI((VulcanVoting) plugin);
        }
    }

    public static void cleanup() {
        instance = null;
    }
}
