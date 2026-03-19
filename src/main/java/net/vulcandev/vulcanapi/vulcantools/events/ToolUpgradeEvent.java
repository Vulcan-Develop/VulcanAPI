package net.vulcandev.vulcanapi.vulcantools.events;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.wrapper.ToolTypeWrapper;
import net.vulcandev.vulcantools.enums.ToolType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Event fired when a player upgrades a VulcanTools tool enchantment
 */
@Getter
public class ToolUpgradeEvent extends VulcanEvent implements Cancellable {
    private final Player player;
    private final ItemStack tool;
    private final ToolTypeWrapper toolType;
    private final String enchantmentId;
    private final int oldLevel;
    @Setter
    private int newLevel;
    @Setter
    private long upgradeCost;
    @Setter
    private String currencyType;
    @Setter
    private boolean cancelled;
    
    /**
     * Creates a new ToolUpgradeEvent.
     *
     * @param player the player upgrading the tool
     * @param tool the tool being upgraded
     * @param toolType the type of tool being upgraded
     * @param enchantmentId the ID of the enchantment being upgraded
     * @param oldLevel the previous level of the enchantment
     * @param newLevel the new level of the enchantment
     * @param upgradeCost the cost of the upgrade
     * @param currencyType the type of currency used for the upgrade
     */
    public ToolUpgradeEvent(Player player, ItemStack tool, ToolType toolType, String enchantmentId, int oldLevel, int newLevel, long upgradeCost, String currencyType) {
        this.player = player;
        this.tool = tool;
        this.toolType = ToolTypeWrapper.fromVulcanToolType(toolType);
        this.enchantmentId = enchantmentId;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
        this.upgradeCost = upgradeCost;
        this.currencyType = currencyType;
        this.cancelled = false;
    }

    /**
     * Gets the enchantment that is being upgraded (alias for getEnchantmentId).
     *
     * @return the enchantment identifier
     */
    public String getEnchantment() {
        return enchantmentId;
    }

    /**
     * Gets the level increase from this upgrade.
     *
     * @return the number of levels being added
     */
    public int getLevelIncrease() {
        return newLevel - oldLevel;
    }

    /**
     * Checks if this is a new enchantment (upgrading from level 0).
     *
     * @return true if this is a new enchantment, false if upgrading existing
     */
    public boolean isNewEnchantment() {
        return oldLevel == 0;
    }
    @Override
    public boolean isCancellable() {
        return true;
    }
}
