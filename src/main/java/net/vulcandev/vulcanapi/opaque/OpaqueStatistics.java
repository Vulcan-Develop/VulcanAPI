package net.vulcandev.vulcanapi.opaque;

import org.jetbrains.annotations.ApiStatus;

/** What Opaque is concealing now, and running totals since it last enabled. */
public final class OpaqueStatistics {

    /** Every value zero, returned while Opaque is not available. */
    public static final OpaqueStatistics EMPTY = new OpaqueStatistics(0, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);

    private final int concealedPairs;
    private final long trackedBlocks;
    private final long hiddenBaseBlocks;
    private final long playersConcealed;
    private final long playersRevealed;
    private final long blocksConcealed;
    private final long blocksRevealed;
    private final long entityPacketsWithheld;
    private final long blockEventsWithheld;
    private final long interactionsBlocked;

    @ApiStatus.Internal
    public OpaqueStatistics(int concealedPairs, long trackedBlocks, long hiddenBaseBlocks,
                            long playersConcealed, long playersRevealed, long blocksConcealed,
                            long blocksRevealed, long entityPacketsWithheld, long blockEventsWithheld,
                            long interactionsBlocked) {
        this.concealedPairs = concealedPairs;
        this.trackedBlocks = trackedBlocks;
        this.hiddenBaseBlocks = hiddenBaseBlocks;
        this.playersConcealed = playersConcealed;
        this.playersRevealed = playersRevealed;
        this.blocksConcealed = blocksConcealed;
        this.blocksRevealed = blocksRevealed;
        this.entityPacketsWithheld = entityPacketsWithheld;
        this.blockEventsWithheld = blockEventsWithheld;
        this.interactionsBlocked = interactionsBlocked;
    }

    /** Viewer and player pairs where the player's body is hidden from the viewer right now. */
    public int getConcealedPairs() { return concealedPairs; }

    /** Protected blocks anti-xray is watching for all players right now. */
    public long getTrackedBlocks() { return trackedBlocks; }

    /** Blocks the base hider is sending as fill to all players right now. */
    public long getHiddenBaseBlocks() { return hiddenBaseBlocks; }

    /** Times a player was hidden from a viewer. */
    public long getPlayersConcealed() { return playersConcealed; }

    /** Times a hidden player was shown again. */
    public long getPlayersRevealed() { return playersRevealed; }

    /** Blocks sent to players as a replacement or base fill instead of the real block, chunks included. */
    public long getBlocksConcealed() { return blocksConcealed; }

    /** Concealed blocks shown to players once they came into sight or reach. */
    public long getBlocksRevealed() { return blocksRevealed; }

    /** Entity packets kept from viewers who could not see the entity. */
    public long getEntityPacketsWithheld() { return entityPacketsWithheld; }

    /** Block events, such as chest opening animations, kept from viewers who could not see the block. */
    public long getBlockEventsWithheld() { return blockEventsWithheld; }

    /** Attacks and interactions refused because they targeted a hidden player. */
    public long getInteractionsBlocked() { return interactionsBlocked; }
}
