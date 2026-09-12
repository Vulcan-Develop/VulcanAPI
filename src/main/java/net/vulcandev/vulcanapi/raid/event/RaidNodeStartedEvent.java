package net.vulcandev.vulcanapi.raid.event;

import net.vulcandev.vulcanapi.raid.RaidNodeEvent;
import net.vulcandev.vulcanapi.raid.RaidNodeView;

public final class RaidNodeStartedEvent extends RaidNodeEvent {
    public RaidNodeStartedEvent(RaidNodeView node) {
        super(node);
    }
}
