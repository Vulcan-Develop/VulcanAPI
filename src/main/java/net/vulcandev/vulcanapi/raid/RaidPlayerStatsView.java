package net.vulcandev.vulcanapi.raid;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public final class RaidPlayerStatsView {
    private final UUID playerId;
    private final long kills;
    private final long deaths;
    private final long blocksPlaced;
    private final long blocksCaught;
    private final long hitsDealt;
    private final long hitsTaken;
    private final double damageDealt;
    private final double damageTaken;
    private final String factionSide;
}
