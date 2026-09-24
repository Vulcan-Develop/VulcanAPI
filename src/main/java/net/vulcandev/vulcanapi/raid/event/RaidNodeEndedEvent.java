package net.vulcandev.vulcanapi.raid.event;

import net.vulcandev.vulcanapi.raid.RaidEndReason;
import net.vulcandev.vulcanapi.raid.RaidNodeEvent;
import net.vulcandev.vulcanapi.raid.RaidNodeView;
import org.jetbrains.annotations.ApiStatus;

public final class RaidNodeEndedEvent extends RaidNodeEvent {
    private final RaidEndReason reason;

    @ApiStatus.Internal
    public RaidNodeEndedEvent(RaidNodeView node, RaidEndReason reason) {
        super(node);
        this.reason = reason;
    }

    public RaidEndReason getReason() { return reason; }
}
