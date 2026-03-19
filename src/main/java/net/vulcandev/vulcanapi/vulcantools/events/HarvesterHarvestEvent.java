package net.vulcandev.vulcanapi.vulcantools.events;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.wrapper.ToolModeWrapper;
import net.vulcandev.vulcantools.enums.ToolMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

/**
 * Event fired when a player harvests crops using a VulcanTools harvester hoe
 */
@Getter
public class HarvesterHarvestEvent extends VulcanEvent implements Cancellable {
    private final Player player;
    private final List<Block> harvestedBlocks;
    private final Material cropType;
    private final BlockBreakEvent originalEvent;
    private final ToolModeWrapper toolMode;
    @Setter
    private int amount;
    @Setter
    private boolean cancelled;

    /**
     * Creates a new HarvesterHarvestEvent.
     *
     * @param player the player who harvested the crops
     * @param harvestedBlocks the list of blocks that were harvested
     * @param cropType the type of crop that was harvested
     * @param originalEvent the original Bukkit BlockBreakEvent
     * @param toolMode the tool mode of the harvester hoe used
     * @param amount the amount of crops harvested
     */
    public HarvesterHarvestEvent(Player player, List<Block> harvestedBlocks, Material cropType, BlockBreakEvent originalEvent, ToolMode toolMode, int amount) {
        this.player = player;
        this.harvestedBlocks = harvestedBlocks;
        this.cropType = cropType;
        this.originalEvent = originalEvent;
        this.toolMode = ToolModeWrapper.fromVulcanToolMode(toolMode);
        this.amount = amount;
        this.cancelled = false;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
