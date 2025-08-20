package net.vulcandev.vulcanapi.vulcanvoting;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerVoteEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    
    private final Player player;
    private final String serviceName;
    private final String username;
    private final String address;
    private final long timestamp;
    private boolean cancelled = false;
    
    public PlayerVoteEvent(@NotNull Player player, @NotNull String serviceName, 
                          @NotNull String username, @NotNull String address, long timestamp) {
        this.player = player;
        this.serviceName = serviceName;
        this.username = username;
        this.address = address;
        this.timestamp = timestamp;
    }
    
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    @NotNull
    public String getServiceName() {
        return serviceName;
    }
    
    @NotNull
    public String getUsername() {
        return username;
    }
    
    @NotNull
    public String getAddress() {
        return address;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}