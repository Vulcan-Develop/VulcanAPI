package net.vulcandev.vulcanapi.raid.event;

import lombok.Getter;
import net.vulcandev.vulcanapi.raid.RaidNodeEvent;
import net.vulcandev.vulcanapi.raid.RaidNodeView;

@Getter
public final class RaidPhaseChangedEvent extends RaidNodeEvent {
    private final String previousPhaseId;

    public RaidPhaseChangedEvent(RaidNodeView node, String previousPhaseId) {
        super(node);
        this.previousPhaseId = previousPhaseId;
    }

}
