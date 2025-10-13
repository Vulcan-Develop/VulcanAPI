package net.vulcandev.vulcanapi.vulcanenchants;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CEBuyEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    
    private final Player player;
    private final String enchantName;
    private final ItemStack bookItem;
    private double cost;
    private boolean cancelled = false;
    
    public CEBuyEvent(@NotNull Player player, @NotNull String enchantName, @NotNull ItemStack bookItem, double cost) {
        this.player = player;
        this.enchantName = enchantName;
        this.bookItem = bookItem.clone();
        this.cost = cost;
    }
    
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    @NotNull
    public String getEnchantName() {
        return enchantName;
    }
    
    @NotNull
    public ItemStack getBookItem() {
        return bookItem.clone();
    }
    
    public double getCost() {
        return cost;
    }
    
    public void setCost(double cost) {
        this.cost = cost;
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