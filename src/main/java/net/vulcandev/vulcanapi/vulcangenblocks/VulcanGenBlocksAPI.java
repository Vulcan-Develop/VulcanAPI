package net.vulcandev.vulcanapi.vulcangenblocks;

import lombok.Getter;
import net.vulcandev.genblocks.VulcanGenBlocks;
import org.jetbrains.annotations.ApiStatus;

public class VulcanGenBlocksAPI {
    @ApiStatus.Internal
    public static VulcanGenBlocksAPI instance;

    @Getter
    @ApiStatus.Internal
    private final VulcanGenBlocks plugin;

    @ApiStatus.Internal
    public VulcanGenBlocksAPI(VulcanGenBlocks plugin) {
        this.plugin = plugin;
    }

    public static VulcanGenBlocksAPI getInstance() {
        return instance;
    }

    public static boolean isAvailable() {
        return instance != null && instance.plugin != null;
    }

    @ApiStatus.Internal
    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin.getClass().getName().equals("net.vulcandev.genblocks.VulcanGenBlocks")) {
            instance = new VulcanGenBlocksAPI((VulcanGenBlocks) plugin);
        }
    }

    @ApiStatus.Internal
    public static void cleanup() {
        instance = null;
    }
}
