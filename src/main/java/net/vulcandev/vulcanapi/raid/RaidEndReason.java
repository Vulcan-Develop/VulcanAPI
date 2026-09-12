package net.vulcandev.vulcanapi.raid;

public enum RaidEndReason {
    EXPIRED,
    CANCELLED,
    PARENT_ENDED,
    BREACHED,
    CAPTURED,
    ADMIN,
    SHUTDOWN,
    UNKNOWN
}
