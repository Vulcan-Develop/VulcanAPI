package net.vulcandev.vulcanapi.raid.event;

import net.vulcandev.vulcanapi.raid.RaidNodeEvent;
import net.vulcandev.vulcanapi.raid.RaidNodeView;
import org.jetbrains.annotations.ApiStatus;

public final class RaidNodeStartedEvent extends RaidNodeEvent {
    @ApiStatus.Internal
    public RaidNodeStartedEvent(RaidNodeView node) {
        super(node);
    }
}
