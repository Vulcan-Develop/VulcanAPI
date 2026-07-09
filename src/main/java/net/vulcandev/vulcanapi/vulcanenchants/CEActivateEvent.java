package net.vulcandev.vulcanapi.vulcanenchants;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CEActivateEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Player player;
    @Getter
    private final String enchantName;
    private final ItemStack item;
    @Getter
    private final ActivationType activationType;

    @Getter
    @Setter
    private boolean cancelled = false;

    public enum ActivationType {
        ON_DAMAGE,
        ON_KILL,
        ON_BLOCK_BREAK,
        ON_MOVE,
        ON_DAMAGE_TAKEN,
        ON_EQUIP,
        ON_UNEQUIP,
        OTHER
    }

    public CEActivateEvent(@NotNull Player player, @NotNull String enchantName, @Nullable ItemStack item, @NotNull ActivationType activationType) {
        this.player = player;
        this.enchantName = enchantName;
        this.item = item != null ? item.clone() : null;
        this.activationType = activationType;
    }

    @Nullable
    public ItemStack getItem() {
        return item != null ? item.clone() : null;
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
