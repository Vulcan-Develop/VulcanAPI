package net.vulcandev.vulcanapi.vulcantools.events;

import net.vulcandev.vulcanapi.wrapper.ToolTypeWrapper;
import net.vulcandev.vulcantools.enums.ToolType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a player upgrades a VulcanTools tool enchantment
 */
public class ToolUpgradeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final ItemStack tool;
    private final ToolTypeWrapper toolType;
    private final String enchantmentId;
    private final int oldLevel;
    private int newLevel;
    private long upgradeCost;
    private String currencyType;
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
     * Gets the player upgrading the tool.
     *
     * @return the player upgrading the tool
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the tool being upgraded.
     *
     * @return the tool ItemStack
     */
    public ItemStack getTool() {
        return tool;
    }

    /**
     * Gets the type of tool being upgraded.
     *
     * @return the tool type
     */
    public ToolTypeWrapper getToolType() {
        return toolType;
    }

    /**
     * Gets the ID of the enchantment being upgraded.
     *
     * @return the enchantment identifier
     */
    public String getEnchantmentId() {
        return enchantmentId;
    }

    /**
     * Gets the previous level of the enchantment.
     *
     * @return the old enchantment level
     */
    public int getOldLevel() {
        return oldLevel;
    }

    /**
     * Gets the new level of the enchantment.
     *
     * @return the new enchantment level
     */
    public int getNewLevel() {
        return newLevel;
    }

    /**
     * Sets the new level of the enchantment.
     *
     * @param newLevel the new enchantment level
     */
    public void setNewLevel(int newLevel) {
        this.newLevel = newLevel;
    }

    /**
     * Gets the cost of the upgrade.
     *
     * @return the upgrade cost
     */
    public long getUpgradeCost() {
        return upgradeCost;
    }

    /**
     * Sets the cost of the upgrade.
     *
     * @param upgradeCost the new upgrade cost
     */
    public void setUpgradeCost(long upgradeCost) {
        this.upgradeCost = upgradeCost;
    }

    /**
     * Gets the type of currency used for the upgrade.
     *
     * @return the currency type
     */
    public String getCurrencyType() {
        return currencyType;
    }

    /**
     * Sets the type of currency used for the upgrade.
     *
     * @param currencyType the new currency type
     */
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
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
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
