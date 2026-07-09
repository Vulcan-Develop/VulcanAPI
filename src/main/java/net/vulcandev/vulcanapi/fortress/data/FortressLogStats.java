package net.vulcandev.vulcanapi.fortress.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public final class FortressLogStats {
    private final long totalFlags;
    private final long uniquePlayers;
    private final int maxVl;
    private final int avgPing;
    private final long lastFlagAt;
    private final List<CheckCount> checkCounts;
    private final List<TopPlayer> topPlayers;

    public FortressLogStats(long totalFlags, long uniquePlayers, int maxVl, int avgPing, long lastFlagAt, List<CheckCount> checkCounts, List<TopPlayer> topPlayers) {
        this.totalFlags = totalFlags;
        this.uniquePlayers = uniquePlayers;
        this.maxVl = maxVl;
        this.avgPing = avgPing;
        this.lastFlagAt = lastFlagAt;
        this.checkCounts = checkCounts == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<>(checkCounts));
        this.topPlayers = topPlayers == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<>(topPlayers));
    }

    @Getter
    @AllArgsConstructor
    public static final class CheckCount {
        private final String checkName;
        private final String checkType;
        private final long count;
    }

    @Getter
    @AllArgsConstructor
    public static final class TopPlayer {
        private final UUID uuid;
        private final long flags;
        private final int maxVl;
        private final long lastFlagAt;
    }
}
