package net.vulcandev.vulcanapi.vulcanenchants;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CEApplyEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    
    private final Player player;
    private final String enchantName;
    private final int level;
    private final ItemStack targetItem;
    private final ItemStack resultItem;
    private boolean cancelled = false;
    
    public CEApplyEvent(@NotNull Player player, @NotNull String enchantName, int level, 
                       @NotNull ItemStack targetItem, @NotNull ItemStack resultItem) {
        this.player = player;
        this.enchantName = enchantName;
        this.level = level;
        this.targetItem = targetItem.clone();
        this.resultItem = resultItem.clone();
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
    
    @NotNull
    public ItemStack getResultItem() {
        return resultItem.clone();
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