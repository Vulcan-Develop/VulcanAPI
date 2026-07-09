package net.vulcandev.vulcanapi.vulcangenblocks;

import lombok.Getter;
import net.vulcandev.genblocks.VulcanGenBlocks;

public class VulcanGenBlocksAPI {
    public static VulcanGenBlocksAPI instance;

    @Getter
    private final VulcanGenBlocks plugin;

    public VulcanGenBlocksAPI(VulcanGenBlocks plugin) {
        this.plugin = plugin;
    }

    public static VulcanGenBlocksAPI getInstance() {
        return instance;
    }

    public static boolean isAvailable() {
        return instance != null && instance.plugin != null;
    }

    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin.getClass().getName().equals("net.vulcandev.genblocks.VulcanGenBlocks")) {
            instance = new VulcanGenBlocksAPI((VulcanGenBlocks) plugin);
        }
    }

    public static void cleanup() {
        instance = null;
    }
}
