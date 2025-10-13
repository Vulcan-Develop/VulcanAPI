package net.vulcandev.vulcanapi.vulcanenchants;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a custom enchant is activated/triggered.
 * This event is fired when an enchant effect is triggered through player actions.
 * For example: Beheading triggering on kill, JellyLegs preventing fall damage, etc.
 */
public class CEActivateEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final String enchantName;
    private final ItemStack item;
    private final ActivationType activationType;
    private boolean cancelled = false;

    public enum ActivationType {
        /** Enchant activated on entity damage */
        ON_DAMAGE,
        /** Enchant activated on entity death/kill */
        ON_KILL,
        /** Enchant activated on block break */
        ON_BLOCK_BREAK,
        /** Enchant activated on player movement */
        ON_MOVE,
        /** Enchant activated when player takes damage */
        ON_DAMAGE_TAKEN,
        /** Enchant activated on item equip */
        ON_EQUIP,
        /** Enchant activated on item unequip */
        ON_UNEQUIP,
        /** Other/generic activation */
        OTHER
    }

    public CEActivateEvent(@NotNull Player player, @NotNull String enchantName, @Nullable ItemStack item, @NotNull ActivationType activationType) {
        this.player = player;
        this.enchantName = enchantName;
        this.item = item != null ? item.clone() : null;
        this.activationType = activationType;
    }

    /**
     * Gets the player who activated the enchant.
     *
     * @return the player
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the name/key of the enchant that was activated.
     *
     * @return the enchant name
     */
    @NotNull
    public String getEnchantName() {
        return enchantName;
    }

    /**
     * Gets the item that has the enchant.
     *
     * @return the item with the enchant, or null if not applicable
     */
    @Nullable
    public ItemStack getItem() {
        return item != null ? item.clone() : null;
    }

    /**
     * Gets the type of activation that triggered this enchant.
     *
     * @return the activation type
     */
    @NotNull
    public ActivationType getActivationType() {
        return activationType;
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
