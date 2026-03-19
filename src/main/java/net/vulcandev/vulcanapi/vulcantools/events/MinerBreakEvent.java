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
 * Event fired when a player breaks blocks using a VulcanTools Miner Pickaxe.
 * This event is fired synchronously before blocks are removed, allowing cancellation.
 */
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

    /**
     * Creates a new MinerBreakEvent.
     *
     * @param player        the player who broke the blocks
     * @param brokenBlocks  the list of blocks that were broken (includes vein mined blocks)
     * @param blockType     the type of block that was broken
     * @param originalEvent the original Bukkit BlockBreakEvent
     * @param toolMode      the tool mode of the miner pickaxe used
     * @param amount        the total number of blocks broken
     */
    public MinerBreakEvent(Player player, List<Block> brokenBlocks, Material blockType, BlockBreakEvent originalEvent, ToolMode toolMode, int amount) {
        this.player = player;
        this.brokenBlocks = brokenBlocks;
        this.blockType = blockType;
        this.originalEvent = originalEvent;
        this.toolMode = ToolModeWrapper.fromVulcanToolMode(toolMode);
        this.amount = amount;
        this.cancelled = false;
    }

    /**
     * Gets the amount of blocks broken (alias for getAmount).
     *
     * @return the number of blocks broken
     */
    public int getBrokenAmount() {
        return amount;
    }

    /**
     * Gets the original Bukkit BlockBreakEvent (alias for getOriginalEvent).
     *
     * @return the original block break event
     */
    public BlockBreakEvent getBukkitEvent() {
        return originalEvent;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
