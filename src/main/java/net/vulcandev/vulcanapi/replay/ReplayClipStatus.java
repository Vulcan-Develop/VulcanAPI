package net.vulcandev.vulcanapi.replay;

public enum ReplayClipStatus {
    PROCESSING,
    LOCAL_SAVED,
    LOCAL_SAVED_PENDING_UPLOAD,
    PUBLISHED,
    LOCAL_ONLY_QUOTA_EXCEEDED,
    DEGRADED,
    FAILED,
    DELETED
}
