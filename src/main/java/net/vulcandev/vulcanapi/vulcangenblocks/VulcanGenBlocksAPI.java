package net.vulcandev.vulcanapi.vulcangenblocks;

import net.vulcandev.genblocks.VulcanGenBlocks;

public class VulcanGenBlocksAPI {
    public static VulcanGenBlocksAPI instance;
    private final VulcanGenBlocks plugin;

    public VulcanGenBlocksAPI(VulcanGenBlocks plugin) {this.plugin = plugin;}

    public static VulcanGenBlocksAPI getInstance() {return instance;}

    public static boolean isAvailable() {return instance != null && instance.plugin != null;}


    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin.getClass().getName().equals("net.vulcandev.genblocks.VulcanGenBlocks")) {
            VulcanGenBlocks vulcanGenBlocks = (VulcanGenBlocks) plugin;
            instance = new VulcanGenBlocksAPI(vulcanGenBlocks);
        }
    }

    public static void cleanup() {instance = null;}
}
