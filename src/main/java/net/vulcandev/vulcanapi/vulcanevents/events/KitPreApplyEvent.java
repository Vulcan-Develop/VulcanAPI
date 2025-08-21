package net.vulcandev.vulcanapi.vulcanevents.events;

import net.vulcandev.vulcanapi.wrapper.EventTypeWrapper;
import net.vulcandev.vulcanevents.interfaces.IEvent;
import net.vulcandev.vulcanevents.interfaces.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Called before a kit is applied to a player in a Vulcan Event.
 * <p>
 * This event is {@link Cancellable} – if cancelled, the kit will not be applied
 * to the player.
 */
public class KitPreApplyEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final EventTypeWrapper eventType;
    private final String eventName;
    private boolean cancelled = false;
    private final ItemStack offHand;
    private final List<ItemStack> armor;
    private final List<ItemStack> items;

    public KitPreApplyEvent(@NotNull Player player, @NotNull Kit kit, @NotNull IEvent event) {
        this.player = player;
        this.offHand = kit.getOffhand();
        this.armor = kit.getArmor();
        this.items = kit.getItems();
        this.eventType = EventTypeWrapper.fromVulcanEventType(event.getEventType());
        this.eventName = event.getName();
    }

    /**
     * Gets the player that is about to receive the kit.
     *
     * @return the {@link Player}
     */
    @NotNull
    public Player getPlayer() {
        return player;
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

    /**
     * Gets the full set of armor pieces defined by the kit.
     * <p>
     * The list typically contains up to four {@link ItemStack}s in the order:
     * Helmet, Chestplate, Leggings, Boots. Some positions may be {@code null}
     * if the kit does not provide that equipment.
     *
     * @return a list of armor {@link ItemStack}s, possibly containing nulls
     */
    public List<ItemStack> getArmor() {
        return armor;
    }

    /**
     * Gets the inventory items defined by the kit.
     * <p>
     * This corresponds to the main inventory slots (storage and hotbar)
     * that will be given to the player if the event is not cancelled.
     *
     * @return a list of {@link ItemStack}s representing the kit's items
     */
    public List<ItemStack> getItems() {
        return items;
    }

    /**
     * Gets the off-hand item defined by the kit.
     *
     * @return the {@link ItemStack} for the off-hand, or {@code null} if none
     */
    public ItemStack getOffHand() {
        return offHand;
    }

    /**
     * Checks whether the event has been cancelled.
     * <p>
     * If {@code true}, the kit will not be applied to the player.
     *
     * @return {@code true} if cancelled, {@code false} otherwise
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether this event should be cancelled.
     * <p>
     * If set to {@code true}, the kit application will be prevented.
     *
     * @param cancelled {@code true} to cancel, {@code false} to allow
     */
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