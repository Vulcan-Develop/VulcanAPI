package net.vulcandev.vulcanapi.vulcantools.events;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.wrapper.ToolTypeWrapper;
import net.vulcandev.vulcantools.enums.ToolType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    public String getEnchantment() {
        return enchantmentId;
    }

    public int getLevelIncrease() {
        return newLevel - oldLevel;
    }

    public boolean isNewEnchantment() {
        return oldLevel == 0;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
