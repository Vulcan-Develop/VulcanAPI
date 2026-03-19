package net.vulcandev.vulcanapi.vulcantools.events;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.wrapper.ToolModeWrapper;
import net.vulcandev.vulcanapi.wrapper.ToolTypeWrapper;
import net.vulcandev.vulcantools.enums.ToolMode;
import net.vulcandev.vulcantools.enums.ToolType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Event fired when a player changes the mode of a VulcanTools tool
 */
@Getter
public class ToolModeChangeEvent extends VulcanEvent implements Cancellable {
    private final Player player;
    private final ItemStack tool;
    private final ToolTypeWrapper toolType;
    private final ToolModeWrapper oldMode;
    @Setter
    private ToolModeWrapper newMode;
    @Setter
    private boolean cancelled;
    
    /**
     * Creates a new ToolModeChangeEvent.
     *
     * @param player the player changing the tool mode
     * @param tool the tool whose mode is being changed
     * @param toolType the type of tool
     * @param oldMode the previous mode of the tool
     * @param newMode the new mode of the tool
     */
    public ToolModeChangeEvent(Player player, ItemStack tool, ToolType toolType, ToolMode oldMode, ToolMode newMode) {
        this.player = player;
        this.tool = tool;
        this.toolType = ToolTypeWrapper.fromVulcanToolType(toolType);
        this.oldMode = ToolModeWrapper.fromVulcanToolMode(oldMode);
        this.newMode = ToolModeWrapper.fromVulcanToolMode(newMode);
        this.cancelled = false;
    }

    /**
     * Checks if the mode is actually changing.
     *
     * @return true if the mode is different, false if it's the same
     */
    public boolean isModeChanging() {
        return oldMode != newMode;
    }
    @Override
    public boolean isCancellable() {
        return true;
    }
}
