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
public class LumberHarvestEvent extends VulcanEvent implements Cancellable {
    private final Player player;
    private final List<Block> harvestedBlocks;
    private final Material woodType;
    private final BlockBreakEvent originalEvent;
    private final ToolModeWrapper toolMode;
    @Setter
    private int amount;
    @Setter
    private boolean cancelled;

    public LumberHarvestEvent(Player player, List<Block> harvestedBlocks, Material woodType, BlockBreakEvent originalEvent, ToolMode toolMode, int amount) {
        this.player = player;
        this.harvestedBlocks = harvestedBlocks;
        this.woodType = woodType;
        this.originalEvent = originalEvent;
        this.toolMode = ToolModeWrapper.fromVulcanToolMode(toolMode);
        this.amount = amount;
        this.cancelled = false;
    }

    public int getHarvestedAmount() {
        return amount;
    }

    public BlockBreakEvent getBukkitEvent() {
        return originalEvent;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
