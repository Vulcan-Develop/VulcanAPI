package net.vulcandev.vulcanapi.raid.event;

import lombok.Getter;
import net.vulcandev.vulcanapi.raid.RaidNodeEvent;
import net.vulcandev.vulcanapi.raid.RaidNodeView;
import org.jetbrains.annotations.ApiStatus;

@Getter
public final class RaidPhaseChangedEvent extends RaidNodeEvent {
    private final String previousPhaseId;

    @ApiStatus.Internal
    public RaidPhaseChangedEvent(RaidNodeView node, String previousPhaseId) {
        super(node);
        this.previousPhaseId = previousPhaseId;
    }

}
