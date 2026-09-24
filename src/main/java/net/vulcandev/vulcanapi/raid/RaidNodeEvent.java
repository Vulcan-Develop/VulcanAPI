package net.vulcandev.vulcanapi.raid;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import org.jetbrains.annotations.ApiStatus;

@Getter
public abstract class RaidNodeEvent extends VulcanEvent {
    private final RaidNodeView node;

    @ApiStatus.Internal
    protected RaidNodeEvent(RaidNodeView node) {
        this.node = node;
    }

    @Override
    public boolean isCancellable() { return false; }
}
