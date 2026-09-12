package net.vulcandev.vulcanapi.raid;

import lombok.Getter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
public final class RaidStatsView {
    private final long shots;
    private final long explosions;
    private final long blocksBroken;
    private final long blocksPlaced;
    private final long kills;
    private final long deaths;
    private final double damageDealt;
    private final double damageTaken;
    private final Map<String, Long> counters;
    private final Map<String, Double> measurements;
    private final long activeMillis;
    private final long firstShotAt;
    private final long lastShotAt;
    private final Set<UUID> participantIds;

    public RaidStatsView(long shots, long explosions, long blocksBroken, long blocksPlaced,
                         long kills, long deaths, double damageDealt, double damageTaken) {
        this(shots, explosions, blocksBroken, blocksPlaced, kills, deaths, damageDealt, damageTaken,
                Collections.<String, Long>emptyMap(), Collections.<String, Double>emptyMap(),
                0L, 0L, 0L, Collections.<UUID>emptySet());
    }

    public RaidStatsView(long shots, long explosions, long blocksBroken, long blocksPlaced,
                         long kills, long deaths, double damageDealt, double damageTaken,
                         Map<String, Long> counters, Map<String, Double> measurements,
                         long activeMillis, long firstShotAt, long lastShotAt) {
        this(shots, explosions, blocksBroken, blocksPlaced, kills, deaths, damageDealt, damageTaken,
                counters, measurements, activeMillis, firstShotAt, lastShotAt,
                Collections.<UUID>emptySet());
    }

    public RaidStatsView(long shots, long explosions, long blocksBroken, long blocksPlaced,
                         long kills, long deaths, double damageDealt, double damageTaken,
                         Map<String, Long> counters, Map<String, Double> measurements,
                         long activeMillis, long firstShotAt, long lastShotAt,
                         Set<UUID> participantIds) {
        this.shots = shots;
        this.explosions = explosions;
        this.blocksBroken = blocksBroken;
        this.blocksPlaced = blocksPlaced;
        this.kills = kills;
        this.deaths = deaths;
        this.damageDealt = damageDealt;
        this.damageTaken = damageTaken;
        this.counters = Collections.unmodifiableMap(new LinkedHashMap<>(
                counters == null ? Collections.<String, Long>emptyMap() : counters));
        this.measurements = Collections.unmodifiableMap(new LinkedHashMap<>(
                measurements == null ? Collections.<String, Double>emptyMap() : measurements));
        this.activeMillis = activeMillis;
        this.firstShotAt = firstShotAt;
        this.lastShotAt = lastShotAt;
        this.participantIds = Collections.unmodifiableSet(new LinkedHashSet<>(
                participantIds == null ? Collections.<UUID>emptySet() : participantIds));
    }

    public long getCounter(String key) {
        Long value = counters.get(key);
        return value == null ? 0L : value;
    }

    public double getMeasurement(String key) {
        Double value = measurements.get(key);
        return value == null ? 0D : value;
    }

    public long getAcceptedShots() { return getCounter("accepted_shots"); }
    public long getBlockedShots() { return getCounter("blocked_shots"); }
    public long getDryShots() { return getCounter("dry_shots"); }
    public long getTntConsumed() { return getCounter("tnt_consumed"); }
    public long getCannonDetections() { return getCounter("cannon_detections"); }
    public long getCannonSwitches() { return getCounter("cannon_switches"); }
    public long getBreaches() { return getCounter("breaches"); }
    public long getPhaseChanges() { return getCounter("phase_changes"); }
    public long getCounterStarts() { return getCounter("counter_starts"); }
    public long getSideCounterStarts() { return getCounter("side_counter_starts"); }
    public long getNestedCounterStarts() { return getCounter("nested_counter_starts"); }
    public long getGenBucketsPlaced() { return getCounter("genbuckets_placed"); }
    public long getSpawnersRemoved() { return getCounter("spawners_removed"); }
    public long getChunkbusts() { return getCounter("chunkbusts"); }
    public long getGuardDenials() { return getCounter("guard_denials"); }
    public long getLeechDamageBlocked() { return getCounter("leech_damage_blocked"); }
    public long getLeechPickupsBlocked() { return getCounter("leech_pickups_blocked"); }
    public long getClaimChanges() { return getCounter("claim_changes"); }
    public long getPeakParticipants() { return getCounter("peak_participants"); }
    public long getUniqueParticipants() {
        return participantIds.isEmpty() ? getCounter("unique_participants") : participantIds.size();
    }
    public double getAverageBlocksPerExplosion() { return getMeasurement("average_blocks_per_explosion"); }
    public double getShotsPerMinute() { return getMeasurement("shots_per_minute"); }
    public double getMaxBlocksPerExplosion() { return getMeasurement("max_blocks_per_explosion"); }
    public double getMaxBreachDistance() { return getMeasurement("max_breach_distance"); }
}
