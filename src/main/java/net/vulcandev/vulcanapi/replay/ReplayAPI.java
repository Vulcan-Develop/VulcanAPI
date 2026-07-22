package net.vulcandev.vulcanapi.replay;

import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.event.VulcanListener;
import net.vulcandev.vulcanapi.replay.data.ReplayBufferView;
import net.vulcandev.vulcanapi.replay.data.ReplayClipView;
import net.vulcandev.vulcanapi.replay.data.ReplayMarkerRequest;
import net.vulcandev.vulcanapi.replay.data.ReplayPage;
import net.vulcandev.vulcanapi.replay.data.ReplayQuery;
import net.vulcandev.vulcanapi.replay.data.ReplaySaveRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/** Public, implementation-independent contract exposed by the VulcanReplay plugin. */
public interface ReplayAPI {

    void registerListener(VulcanListener listener);

    void unregisterListener(VulcanListener listener);

    boolean callEvent(VulcanEvent event);

    CompletableFuture<ReplayClipView> saveLast(ReplaySaveRequest request);

    CompletableFuture<ReplayClipView> addMarker(ReplayMarkerRequest request);

    CompletableFuture<ReplayBufferView> getBuffer(UUID playerId);

    CompletableFuture<ReplayClipView> getClip(String clipId);

    CompletableFuture<ReplayPage<ReplayClipView>> getClips(ReplayQuery query);

    CompletableFuture<ReplayClipView> publish(String clipId);

    CompletableFuture<Boolean> delete(String clipId, ReplayDeleteScope scope);

    String getVersion();

    boolean isEnabled();

    static ReplayAPI getInstance() {
        return ReplayAPIInstance.getInstance();
    }

    class ReplayAPIInstance {
        private static volatile ReplayAPI replayAPI;

        public static ReplayAPI getInstance() {
            return replayAPI;
        }

        public static void setInstance(ReplayAPI value) {
            replayAPI = value;
        }
    }
}
