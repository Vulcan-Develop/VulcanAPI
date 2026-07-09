package net.vulcandev.vulcanapi.vulcanenchants;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CEBuyEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Player player;
    @Getter
    private final String enchantName;
    private final ItemStack bookItem;

    @Getter
    @Setter
    private double cost;

    @Getter
    @Setter
    private boolean cancelled = false;
    
    public CEBuyEvent(@NotNull Player player, @NotNull String enchantName, @NotNull ItemStack bookItem, double cost) {
        this.player = player;
        this.enchantName = enchantName;
        this.bookItem = bookItem.clone();
        this.cost = cost;
    }

    @NotNull
    public ItemStack getBookItem() {
        return bookItem.clone();
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
