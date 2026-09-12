package net.vulcandev.vulcanapi.raid.event;

import net.vulcandev.vulcanapi.raid.RaidNodeEvent;
import net.vulcandev.vulcanapi.raid.RaidNodeView;

public final class RaidBreachedEvent extends RaidNodeEvent {
    public RaidBreachedEvent(RaidNodeView node) {
        super(node);
    }
}
