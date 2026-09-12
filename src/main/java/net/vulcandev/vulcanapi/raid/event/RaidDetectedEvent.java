package net.vulcandev.vulcanapi.raid.event;

import net.vulcandev.vulcanapi.raid.RaidNodeEvent;
import net.vulcandev.vulcanapi.raid.RaidNodeView;

public final class RaidDetectedEvent extends RaidNodeEvent {
    public RaidDetectedEvent(RaidNodeView node) {
        super(node);
    }
}
