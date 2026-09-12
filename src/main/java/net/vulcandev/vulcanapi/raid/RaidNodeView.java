package net.vulcandev.vulcanapi.raid;

import lombok.Getter;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public final class RaidNodeView {
    private final UUID id;
    private final UUID rootId;
    private final UUID parentId;
    private final RaidNodeType type;
    private final RaidStatus status;
    private final RaidDirection direction;
    private final String targetType;
    private final String targetId;
    private final String defendingFactionId;
    private final String attackingFactionId;
    private final String phaseId;
    private final long startedAt;
    private final long lastActivityAt;
    private final long nextTransitionAt;
    private final long endedAt;
    private final RaidEndReason endReason;
    private final RaidLocation lastLocation;
    private final Set<String> childIds;
    private final Set<String> claimKeys;
    private final RaidStatsView stats;

    public RaidNodeView(UUID id, UUID rootId, UUID parentId, RaidNodeType type, RaidStatus status,
                        RaidDirection direction, String targetType, String targetId,
                        String defendingFactionId, String attackingFactionId, String phaseId,
                        long startedAt, long lastActivityAt, long nextTransitionAt, long endedAt,
                        RaidEndReason endReason, RaidLocation lastLocation, Set<String> childIds,
                        Set<String> claimKeys, RaidStatsView stats) {
        this.id = id;
        this.rootId = rootId;
        this.parentId = parentId;
        this.type = type;
        this.status = status;
        this.direction = direction;
        this.targetType = targetType;
        this.targetId = targetId;
        this.defendingFactionId = defendingFactionId;
        this.attackingFactionId = attackingFactionId;
        this.phaseId = phaseId;
        this.startedAt = startedAt;
        this.lastActivityAt = lastActivityAt;
        this.nextTransitionAt = nextTransitionAt;
        this.endedAt = endedAt;
        this.endReason = endReason;
        this.lastLocation = lastLocation;
        this.childIds = Collections.unmodifiableSet(new LinkedHashSet<>(
                childIds == null ? Collections.<String>emptySet() : childIds));
        this.claimKeys = Collections.unmodifiableSet(new LinkedHashSet<>(
                claimKeys == null ? Collections.<String>emptySet() : claimKeys));
        this.stats = stats == null ? new RaidStatsView(0, 0, 0, 0, 0, 0, 0D, 0D) : stats;
    }

}
