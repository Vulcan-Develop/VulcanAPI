package net.vulcandev.vulcanapi.vulcangenblocks;

import lombok.Getter;
import net.vulcandev.vulcanapi.wrapper.GenWrapper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GenBucketPlaceEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Block placedAgainstBlock;
    @Getter
    private final UUID playerId;
    @Getter
    private final GenWrapper gen;

    public GenBucketPlaceEvent(Block placedBlock, Block placedAgainstBlock, UUID playerId, GenWrapper gen) {
        super(placedBlock);
        this.placedAgainstBlock = placedAgainstBlock;
        this.playerId = playerId;
        this.gen = gen;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerId);
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(playerId);
    }

    public Block getBlockPlaced() {
        return this.getBlock();
    }

    public Block getBlockPlacedAgainst() {
        return this.placedAgainstBlock;
    }

    public boolean getReversed() {
        return gen.isReversed();
    }

    public void setReversed(boolean reversed) {
        gen.setReversed(reversed);
    }

    @Override
    public boolean isCancelled() {
        return this.gen.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.gen.setCancelled(cancel);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
