package net.vulcandev.vulcanapi.replay.event;

import net.vulcandev.vulcanapi.replay.data.ReplayClipView;

public final class ReplaySavedEvent extends ReplayLifecycleEvent {
    private final ReplayClipView clip;

    public ReplaySavedEvent(ReplayClipView clip) {
        super(clip.getClipId());
        this.clip = clip;
    }

    public ReplayClipView getClip() { return clip; }
}
