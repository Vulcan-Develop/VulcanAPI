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
    private final int level;
    private final ItemStack targetItem;
    private double cost;
    private final String currency;
    private boolean cancelled = false;
    
    public CEBuyEvent(@NotNull Player player, @NotNull String enchantName, int level, 
                     @NotNull ItemStack targetItem, double cost, @NotNull String currency) {
        this.player = player;
        this.enchantName = enchantName;
        this.level = level;
        this.targetItem = targetItem.clone();
        this.cost = cost;
        this.currency = currency;
    }
    
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    @NotNull
    public String getEnchantName() {
        return enchantName;
    }
    
    public int getLevel() {
        return level;
    }
    
    @NotNull
    public ItemStack getTargetItem() {
        return targetItem.clone();
    }
    
    public double getCost() {
        return cost;
    }
    
    public void setCost(double cost) {
        this.cost = cost;
    }
    
    @NotNull
    public String getCurrency() {
        return currency;
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