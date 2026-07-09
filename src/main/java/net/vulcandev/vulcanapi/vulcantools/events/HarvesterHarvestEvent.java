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
