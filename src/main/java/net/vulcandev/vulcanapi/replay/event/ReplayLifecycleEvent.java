package net.vulcandev.vulcanapi.replay.event;

import net.vulcandev.vulcanapi.event.VulcanEvent;
import org.jetbrains.annotations.ApiStatus;

public abstract class ReplayLifecycleEvent extends VulcanEvent {
    private final String clipId;

    @ApiStatus.Internal
    protected ReplayLifecycleEvent(String clipId) {
        this.clipId = clipId;
    }

    public String getClipId() { return clipId; }

    @Override
    public boolean isCancellable() { return false; }
}
