package net.vulcandev.vulcanapi.raid.event;

import net.vulcandev.vulcanapi.raid.RaidNodeEvent;
import net.vulcandev.vulcanapi.raid.RaidNodeView;
import org.jetbrains.annotations.ApiStatus;

public final class RaidBreachedEvent extends RaidNodeEvent {
    @ApiStatus.Internal
    public RaidBreachedEvent(RaidNodeView node) {
        super(node);
    }
}
