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
public class MinerBreakEvent extends VulcanEvent implements Cancellable {
    private final Player player;
    private final List<Block> brokenBlocks;
    private final Material blockType;
    private final BlockBreakEvent originalEvent;
    private final ToolModeWrapper toolMode;
    @Setter
    private int amount;
    @Setter
    private boolean cancelled;

    public MinerBreakEvent(Player player, List<Block> brokenBlocks, Material blockType, BlockBreakEvent originalEvent, ToolMode toolMode, int amount) {
        this.player = player;
        this.brokenBlocks = brokenBlocks;
        this.blockType = blockType;
        this.originalEvent = originalEvent;
        this.toolMode = ToolModeWrapper.fromVulcanToolMode(toolMode);
        this.amount = amount;
        this.cancelled = false;
    }

    public int getBrokenAmount() {
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
