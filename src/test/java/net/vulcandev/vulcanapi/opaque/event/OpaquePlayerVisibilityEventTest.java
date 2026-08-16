package net.vulcandev.vulcanapi.opaque.event;

import net.vulcandev.vulcanapi.event.EventHandler;
import net.vulcandev.vulcanapi.event.VulcanEventManager;
import net.vulcandev.vulcanapi.event.VulcanListener;
import net.vulcandev.vulcanapi.opaque.OpaqueAPI;
import org.junit.After;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class OpaquePlayerVisibilityEventTest {

    private final RetainingListener listener = new RetainingListener();

    @After
    public void unregisterListener() {
        OpaqueAPI.unregisterListener(listener);
    }

    @Test
    public void concealedEventCanBeClaimedSynchronously() {
        UUID viewerId = UUID.randomUUID();
        UUID targetId = UUID.randomUUID();
        OpaquePlayerVisibilityEvent event = new OpaquePlayerVisibilityEvent(
                viewerId, targetId, OpaquePlayerVisibilityEvent.State.CONCEALED);
        OpaqueAPI.registerListener(listener);

        OpaqueAPI.callEvent(event);

        assertEquals(viewerId, event.getViewerId());
        assertEquals(targetId, event.getTargetId());
        assertEquals(OpaquePlayerVisibilityEvent.State.CONCEALED, event.getState());
        assertTrue(event.isNametagRetained());
        assertFalse(event.isCancellable());
        assertEquals(1, listener.calls);
    }

    @Test
    public void visibleEventCannotRetainANametag() {
        OpaquePlayerVisibilityEvent event = new OpaquePlayerVisibilityEvent(
                UUID.randomUUID(), UUID.randomUUID(),
                OpaquePlayerVisibilityEvent.State.VISIBLE);

        event.retainNametag();

        assertFalse(event.isNametagRetained());
    }

    private static final class RetainingListener implements VulcanListener {
        private int calls;

        @EventHandler
        public void onVisibility(OpaquePlayerVisibilityEvent event) {
            calls++;
            event.retainNametag();
        }
    }
}
