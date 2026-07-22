package net.vulcandev.vulcanapi.replay.data;

import net.vulcandev.vulcanapi.replay.ReplayClipStatus;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public final class ReplayClipView {
    private final String clipId;
    private final UUID playerId;
    private final long startedAt;
    private final long endedAt;
    private final long compressedBytes;
    private final ReplayClipStatus status;
    private final String source;
    private final String reason;
    private final String sha256;
    private final String viewerUrl;
    private final long localExpiresAt;
    private final long cloudExpiresAt;
    private final Map<String, String> metadata;

    public ReplayClipView(String clipId, UUID playerId, long startedAt, long endedAt,
                          long compressedBytes, ReplayClipStatus status, String source,
                          String reason, String sha256, String viewerUrl, long localExpiresAt,
                          long cloudExpiresAt, Map<String, String> metadata) {
        this.clipId = clipId;
        this.playerId = playerId;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.compressedBytes = compressedBytes;
        this.status = status;
        this.source = source;
        this.reason = reason;
        this.sha256 = sha256;
        this.viewerUrl = viewerUrl;
        this.localExpiresAt = localExpiresAt;
        this.cloudExpiresAt = cloudExpiresAt;
        this.metadata = Collections.unmodifiableMap(new LinkedHashMap<>(
                metadata == null ? Collections.emptyMap() : metadata));
    }

    public String getClipId() { return clipId; }
    public UUID getPlayerId() { return playerId; }
    public long getStartedAt() { return startedAt; }
    public long getEndedAt() { return endedAt; }
    public long getDurationMillis() { return Math.max(0L, endedAt - startedAt); }
    public long getCompressedBytes() { return compressedBytes; }
    public ReplayClipStatus getStatus() { return status; }
    public String getSource() { return source; }
    public String getReason() { return reason; }
    public String getSha256() { return sha256; }
    public String getViewerUrl() { return viewerUrl; }
    public long getLocalExpiresAt() { return localExpiresAt; }
    public long getCloudExpiresAt() { return cloudExpiresAt; }
    public Map<String, String> getMetadata() { return metadata; }
}
