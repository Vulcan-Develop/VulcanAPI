package net.vulcandev.vulcanapi.vulcanevents.events;

import net.vulcandev.vulcanapi.wrapper.EventTypeWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Called after a kit is applied to a player in a VulcanEvent
 */
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

    /**
     * Gets the player receiving the kit.
     *
     * @return the {@link Player} the kit is applied to
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the full set of armor pieces provided by the kit.
     * <p>
     * The returned list can contain up to four {@link ItemStack}s in the order:
     * Helmet, Chestplate, Leggings, Boots. Some entries may be {@code null}
     * if the kit does not provide that piece.
     *
     * @return a list of armor {@link ItemStack}s, possibly containing nulls
     */
    public List<ItemStack> getArmor() {
        return armor;
    }

    /**
     * Gets the inventory items provided by the kit.
     * <p>
     * This represents the main inventory contents (hotbar and storage slots)
     * that the kit gives to the player.
     *
     * @return a list of {@link ItemStack}s representing the kit's items
     */
    public List<ItemStack> getItems() {
        return items;
    }

    /**
     * Gets the off-hand item provided by the kit.
     *
     * @return the {@link ItemStack} placed in the player's off-hand,
     * or {@code null} if the kit does not define one
     */
    public ItemStack getOffHand() {
        return offHand;
    }

    /**
     * Gets the type of event where the kit is being applied.
     *
     * @return the {@link EventTypeWrapper}
     */
    @NotNull
    public EventTypeWrapper getEventType() {
        return eventType;
    }

    /**
     * Gets the name of the event where the kit is being applied.
     *
     * @return the event name
     */
    @NotNull
    public String getEventName() {
        return eventName;
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