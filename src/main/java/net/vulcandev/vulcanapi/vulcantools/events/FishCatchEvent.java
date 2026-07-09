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

    public FishCatchEvent(Player player, @Nullable PlayerFishEvent originalEvent, ToolMode toolMode, int fishAmount) {
        this.player = player;
        this.originalEvent = originalEvent;
        this.toolMode = ToolModeWrapper.fromVulcanToolMode(toolMode);
        this.fishAmount = fishAmount;
        this.cancelled = false;
    }

    public int getCaughtAmount() {
        return fishAmount;
    }

    @Nullable
    public PlayerFishEvent getBukkitEvent() {
        return originalEvent;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
