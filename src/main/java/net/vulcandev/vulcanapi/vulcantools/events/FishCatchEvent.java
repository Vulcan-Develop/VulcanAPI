package net.vulcandev.vulcanapi.vulcantools.events;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.wrapper.ToolModeWrapper;
import net.vulcandev.vulcantools.enums.ToolMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.jetbrains.annotations.Nullable;

/**
 * Event fired when a player catches fish using a VulcanTools fishing rod
 */
@Getter
public class FishCatchEvent extends VulcanEvent implements Cancellable {
    private final Player player;
    @Nullable
    private final PlayerFishEvent originalEvent;
    private final ToolModeWrapper toolMode;
    @Setter
    private int fishAmount;
    @Setter
    private boolean cancelled;
    
    /**
     * Creates a new FishCatchEvent.
     *
     * @param player the player who caught the fish
     * @param originalEvent the original Bukkit PlayerFishEvent, or null when handled by auto-fish
     * @param toolMode the tool mode of the fishing rod used
     * @param fishAmount the number of fish caught
     */
    public FishCatchEvent(Player player, @Nullable PlayerFishEvent originalEvent, ToolMode toolMode, int fishAmount) {
        this.player = player;
        this.originalEvent = originalEvent;
        this.toolMode = ToolModeWrapper.fromVulcanToolMode(toolMode);
        this.fishAmount = fishAmount;
        this.cancelled = false;
    }

    /**
     * Gets the amount of fish caught (alias for getFishAmount).
     *
     * @return the number of fish caught
     */
    public int getCaughtAmount() {
        return fishAmount;
    }

    /**
     * Gets the original Bukkit PlayerFishEvent (alias for getOriginalEvent).
     *
     * @return the original fishing event, or null when handled by auto-fish
     */
    @Nullable
    public PlayerFishEvent getBukkitEvent() {
        return originalEvent;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
