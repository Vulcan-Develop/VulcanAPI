package net.vulcandev.vulcanapi.opaque.event;

import net.vulcandev.vulcanapi.event.VulcanEvent;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A per-viewer player-body visibility transition published synchronously by Opaque.
 * Handlers receive UUIDs because the visible completion may run on a network event loop.
 */
public final class OpaquePlayerVisibilityEvent extends VulcanEvent {

    public enum State {
        CONCEALED,
        VISIBLE
    }

    private final UUID viewerId;
    private final UUID targetId;
    private final State state;
    private final AtomicBoolean nametagRetained = new AtomicBoolean();

    public OpaquePlayerVisibilityEvent(UUID viewerId, UUID targetId, State state) {
        this.viewerId = Objects.requireNonNull(viewerId, "viewerId");
        this.targetId = Objects.requireNonNull(targetId, "targetId");
        this.state = Objects.requireNonNull(state, "state");
    }

    public UUID getViewerId() {
        return viewerId;
    }

    public UUID getTargetId() {
        return targetId;
    }

    public State getState() {
        return state;
    }

    /**
     * Keep an independently tracked nametag through Opaque's imminent player destroy.
     * Has no effect on a {@link State#VISIBLE} notification.
     */
    public void retainNametag() {
        if (state == State.CONCEALED) nametagRetained.set(true);
    }

    public boolean isNametagRetained() {
        return nametagRetained.get();
    }

    @Override
    public boolean isCancellable() {
        return false;
    }
}
