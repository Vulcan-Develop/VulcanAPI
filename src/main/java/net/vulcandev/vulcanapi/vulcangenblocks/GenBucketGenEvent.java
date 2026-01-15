package net.vulcandev.vulcanapi.vulcangenblocks;

import net.vulcandev.vulcanapi.wrapper.GenWrapper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GenBucketGenEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final UUID playerId;
    private final GenWrapper gen;

    public GenBucketGenEvent(UUID playerId, GenWrapper gen) {
        this.playerId = playerId;
        this.gen = gen;
    }

    public Player getPlayer() {return Bukkit.getPlayer(playerId);}
    public OfflinePlayer getOfflinePlayer() {return Bukkit.getOfflinePlayer(playerId);}
    public UUID getPlayerId() {return playerId;}

    public GenWrapper getGen() {return gen;}

    public boolean getReversed() {return gen.isReversed();}
    public void setReversed(boolean reversed) {gen.setReversed(reversed);}

    @Override
    public boolean isCancelled() { return this.gen.isCancelled();}
    @Override
    public void setCancelled(boolean cancel) {
        this.gen.setCancelled(cancel);
    }

    @Override @NotNull
    public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
