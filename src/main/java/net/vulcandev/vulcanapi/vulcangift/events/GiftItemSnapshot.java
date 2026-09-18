package net.vulcandev.vulcanapi.vulcangift.events;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Immutable description of a gifted item. Snapshotting keeps VulcanGift's internals out of the
 * API surface; gifts are stored as serialised strings, so there is no gift object to expose.
 */
@Getter
public final class GiftItemSnapshot {
    private final Material material;
    private final int amount;
    private final short durability;
    private final String displayName;

    public GiftItemSnapshot(Material material, int amount, short durability, String displayName) {
        this.material = material;
        this.amount = amount;
        this.durability = durability;
        this.displayName = displayName;
    }

    /** Builds a snapshot from a live stack; returns null when there is nothing to describe. */
    public static GiftItemSnapshot of(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return null;
        String name = item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                ? item.getItemMeta().getDisplayName()
                : item.getType().name();
        return new GiftItemSnapshot(item.getType(), item.getAmount(), item.getDurability(), name);
    }
}
