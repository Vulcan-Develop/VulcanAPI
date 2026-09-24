package net.vulcandev.vulcanapi.replay.event;

import net.vulcandev.vulcanapi.replay.data.ReplayClipView;
import org.jetbrains.annotations.ApiStatus;

public final class ReplayPublishedEvent extends ReplayLifecycleEvent {
    private final ReplayClipView clip;

    @ApiStatus.Internal
    public ReplayPublishedEvent(ReplayClipView clip) {
        super(clip.getClipId());
        this.clip = clip;
    }

    public ReplayClipView getClip() { return clip; }
}
