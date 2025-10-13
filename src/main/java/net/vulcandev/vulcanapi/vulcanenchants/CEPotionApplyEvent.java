package net.vulcandev.vulcanapi.vulcanenchants;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a potion enchant effect is applied to a player.
 * This happens when a player equips armor with a potion enchant.
 */
public class CEPotionApplyEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final String enchantName;
    private final PotionEffectType potionType;
    private final int amplifier;
    private final ItemStack armorItem;
    private boolean cancelled = false;

    public CEPotionApplyEvent(@NotNull Player player, @NotNull String enchantName, @NotNull PotionEffectType potionType, int amplifier, @NotNull ItemStack armorItem) {
        this.player = player;
        this.enchantName = enchantName;
        this.potionType = potionType;
        this.amplifier = amplifier;
        this.armorItem = armorItem.clone();
    }

    /**
     * Gets the player receiving the potion effect.
     *
     * @return the player
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the enchant name that is applying the effect.
     *
     * @return the enchant name
     */
    @NotNull
    public String getEnchantName() {
        return enchantName;
    }

    /**
     * Gets the potion effect type being applied.
     *
     * @return the potion effect type
     */
    @NotNull
    public PotionEffectType getPotionType() {
        return potionType;
    }

    /**
     * Gets the amplifier level of the potion effect.
     *
     * @return the amplifier (0 = level I, 1 = level II, etc.)
     */
    public int getAmplifier() {
        return amplifier;
    }

    /**
     * Gets the armor item with the enchant.
     *
     * @return the armor item
     */
    @NotNull
    public ItemStack getArmorItem() {
        return armorItem.clone();
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
