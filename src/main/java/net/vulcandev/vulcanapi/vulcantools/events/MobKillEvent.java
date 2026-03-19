package net.vulcandev.vulcanapi.vulcantools.events;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.wrapper.ToolModeWrapper;
import net.vulcandev.vulcantools.enums.ToolMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * Event fired when a player kills a mob using a Mob Sword.
 */
@Getter
public class MobKillEvent extends VulcanEvent {
    private final Player player;
    private final Entity killedEntity;
    private final EntityDeathEvent originalEvent;
    private final ToolModeWrapper toolMode;
    private final int amountKilled;

    public MobKillEvent(Player player, Entity killedEntity, EntityDeathEvent originalEvent, ToolMode toolMode, int amountKilled) {
        this.player = player;
        this.killedEntity = killedEntity;
        this.originalEvent = originalEvent;
        this.toolMode = ToolModeWrapper.fromVulcanToolMode(toolMode);
        this.amountKilled = amountKilled;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }
}
