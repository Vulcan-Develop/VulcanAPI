package net.vulcandev.vulcanapi.replay;

import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.event.VulcanListener;
import net.vulcandev.vulcanapi.replay.data.ReplayBufferView;
import net.vulcandev.vulcanapi.replay.data.ReplayClipView;
import net.vulcandev.vulcanapi.replay.data.ReplayEvidenceRequest;
import net.vulcandev.vulcanapi.replay.data.ReplayMarkerRequest;
import net.vulcandev.vulcanapi.replay.data.ReplayPage;
import net.vulcandev.vulcanapi.replay.data.ReplayQuery;
import net.vulcandev.vulcanapi.replay.data.ReplaySaveRequest;
import org.jetbrains.annotations.ApiStatus;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/** Public, implementation-independent contract exposed by the VulcanReplay plugin. */
public interface ReplayAPI {

    void registerListener(VulcanListener listener);

    void unregisterListener(VulcanListener listener);

    @ApiStatus.Internal
    boolean callEvent(VulcanEvent event);

    CompletableFuture<ReplayClipView> saveLast(ReplaySaveRequest request);

    CompletableFuture<ReplayClipView> addMarker(ReplayMarkerRequest request);

    /**
     * Records structured evidence on the replay's evidence track. A recorder that predates the
     * evidence track keeps the data as a {@code replay.evidence} marker instead, whose metadata
     * holds {@code schema}, {@code evidenceId} and the JSON under {@code evidence}.
     */
    default CompletableFuture<ReplayClipView> addEvidence(ReplayEvidenceRequest request) {
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put("schema", request.getSchema());
        metadata.put("evidenceId", request.getEvidenceId().toString());
        metadata.put("evidence", request.getJson());
        return addMarker(new ReplayMarkerRequest(request.getPlayerId(), null, request.getTimestamp(),
                "replay.evidence", "Evidence", metadata));
    }

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

    @ApiStatus.Internal
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
