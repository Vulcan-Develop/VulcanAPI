package net.vulcandev.vulcanapi.fortress;

import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.event.VulcanListener;
import net.vulcandev.vulcanapi.fortress.data.FortressAltMatch;
import net.vulcandev.vulcanapi.fortress.data.FortressLog;
import net.vulcandev.vulcanapi.fortress.data.FortressLogStats;
import net.vulcandev.vulcanapi.fortress.data.FortressPage;
import net.vulcandev.vulcanapi.fortress.data.FortressPlayerSession;
import net.vulcandev.vulcanapi.fortress.data.FortressPunishment;
import net.vulcandev.vulcanapi.fortress.player.PlayerProfile;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface FortressAPI {

    void registerListener(VulcanListener listener);

    void unregisterListener(VulcanListener listener);

    boolean callEvent(VulcanEvent event);

    PlayerProfile getPlayerProfile(UUID uuid);

    Collection<PlayerProfile> getOnlineProfiles();

    boolean isPlayerMonitored(UUID uuid);

    String getVersion();

    boolean isEnabled();

    default boolean isDatabaseReady() {
        return false;
    }

    default CompletableFuture<List<FortressLog>> getLogs(UUID uuid) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    default CompletableFuture<List<FortressLog>> getLogsSince(long sinceTimestamp) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    default CompletableFuture<FortressPage<FortressLog>> getLogsPage(long since, long until, UUID target, String checkName, String checkType, int offset, int limit) {
        return CompletableFuture.completedFuture(new FortressPage<>(Collections.emptyList(), 0L));
    }

    default CompletableFuture<FortressLogStats> getLogStats(long since, int topChecks, int topPlayers) {
        return CompletableFuture.completedFuture(new FortressLogStats(0L, 0L, 0, 0, 0L, Collections.emptyList(), Collections.emptyList()));
    }

    default CompletableFuture<List<FortressPunishment>> getPunishments(UUID uuid) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    default CompletableFuture<List<FortressPunishment>> getPunishmentsSince(long sinceTimestamp) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    default CompletableFuture<List<FortressPunishment>> getPunishmentsSince(long sinceTimestamp, String checkName, String checkType) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    default CompletableFuture<FortressPage<FortressPunishment>> getPunishmentsPage(long since, long until, UUID target, int offset, int limit) {
        return CompletableFuture.completedFuture(new FortressPage<>(Collections.emptyList(), 0L));
    }

    default CompletableFuture<Long> countPunishmentsSince(long sinceTimestamp) {
        return CompletableFuture.completedFuture(0L);
    }

    default CompletableFuture<FortressPlayerSession> getSession(UUID uuid) {
        return CompletableFuture.completedFuture(null);
    }

    default CompletableFuture<FortressPlayerSession> getSessionByName(String name) {
        return CompletableFuture.completedFuture(null);
    }

    default CompletableFuture<Map<UUID, FortressPlayerSession>> getSessions(Collection<UUID> uuids) {
        return CompletableFuture.completedFuture(new LinkedHashMap<>());
    }

    default CompletableFuture<Map<UUID, FortressPlayerSession>> getSessionSnapshot() {
        return CompletableFuture.completedFuture(new LinkedHashMap<>());
    }

    default CompletableFuture<List<FortressAltMatch>> getAltMatches(UUID uuid) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    static FortressAPI getInstance() {
        return FortressAPIInstance.getInstance();
    }

    class FortressAPIInstance {
        private static FortressAPI fortressAPI;

        public static FortressAPI getInstance() {
            return fortressAPI;
        }

        public static void setInstance(FortressAPI v) {
            fortressAPI = v;
        }
    }
}
