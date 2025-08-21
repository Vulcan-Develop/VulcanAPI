package net.vulcandev.vulcanapi.vulcanenchants;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class CEApplyEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    
    private final Player player;
    private final String enchantName;
    private final int amp;
    private final ItemStack targetItem;
    private final ItemStack bookItem;
    private final PotionEffectType potionEffectType;
    private boolean cancelled = false;
    
    public CEApplyEvent(@NotNull Player player, @NotNull String enchantName, int amp, PotionEffectType potionEffectType,
                       @NotNull ItemStack targetItem, @NotNull ItemStack bookItem) {
        this.player = player;
        this.enchantName = enchantName;
        this.amp = amp;
        this.potionEffectType = potionEffectType;
        this.targetItem = targetItem.clone();
        this.bookItem = bookItem.clone();
    }
    
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    @NotNull
    public String getEnchantName() {
        return enchantName;
    }
    
    public int getAmp() {
        return amp;
    }

    public PotionEffectType getPotionEffectType() { return potionEffectType; }
    
    @NotNull
    public ItemStack getTargetItem() {
        return targetItem.clone();
    }
    
    @NotNull
    public ItemStack getBookItem() {
        return bookItem.clone();
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