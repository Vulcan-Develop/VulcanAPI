package net.vulcandev.vulcanapi.vulcanenchants;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class CEPotionRemoveEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Player player;
    @Getter
    private final String enchantName;
    @Getter
    private final PotionEffectType potionType;
    private final ItemStack armorItem;

    @Getter
    @Setter
    private boolean cancelled = false;

    public CEPotionRemoveEvent(@NotNull Player player, @NotNull String enchantName, @NotNull PotionEffectType potionType, @NotNull ItemStack armorItem) {
        this.player = player;
        this.enchantName = enchantName;
        this.potionType = potionType;
        this.armorItem = armorItem.clone();
    }

    @NotNull
    public ItemStack getArmorItem() {
        return armorItem.clone();
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
