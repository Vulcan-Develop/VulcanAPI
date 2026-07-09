package net.vulcandev.vulcanapi.vulcanevents.events;

import lombok.Getter;
import net.vulcandev.vulcanapi.wrapper.EventTypeWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class KitPostApplyEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final EventTypeWrapper eventType;
    private final String eventName;
    private final ItemStack offHand;
    private final List<ItemStack> armor;
    private final List<ItemStack> items;

    public KitPostApplyEvent(@NotNull Player player, @NotNull EventTypeWrapper eventType, @NotNull String eventName, ItemStack offHand, @NotNull List<ItemStack> armor, @NotNull List<ItemStack> items) {
        this.player = player;
        this.eventType = eventType;
        this.eventName = eventName;
        this.offHand = offHand;
        this.armor = armor;
        this.items = items;
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
