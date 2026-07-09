package net.vulcandev.vulcanapi.vulcantools.events;

import lombok.Getter;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.wrapper.ToolTypeWrapper;
import net.vulcandev.vulcantools.enums.ToolType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ToolEventEndEvent extends VulcanEvent {
    private final ToolTypeWrapper toolType;
    private final LinkedHashMap<UUID, Integer> finalLeaderboard;
    private final int totalParticipants;
    private final boolean wasManuallyEnded;

    public ToolEventEndEvent(ToolType toolType, LinkedHashMap<UUID, Integer> finalLeaderboard, boolean wasManuallyEnded) {
        this.toolType = ToolTypeWrapper.fromVulcanToolType(toolType);
        this.finalLeaderboard = finalLeaderboard;
        this.totalParticipants = finalLeaderboard.size();
        this.wasManuallyEnded = wasManuallyEnded;
    }

    public UUID getWinner() {
        return finalLeaderboard.isEmpty() ? null : finalLeaderboard.entrySet().iterator().next().getKey();
    }

    public int getWinningAmount() {
        return finalLeaderboard.isEmpty() ? 0 : finalLeaderboard.entrySet().iterator().next().getValue();
    }

    public LinkedHashMap<UUID, Integer> getTopPlayers(int limit) {
        LinkedHashMap<UUID, Integer> topPlayers = new LinkedHashMap<>();
        int count = 0;
        for (Map.Entry<UUID, Integer> entry : finalLeaderboard.entrySet()) {
            if (count >= limit) break;
            topPlayers.put(entry.getKey(), entry.getValue());
            count++;
        }
        return topPlayers;
    }

    public boolean wasManuallyEnded() {
        return wasManuallyEnded;
    }

    public String getResultsDescription() {
        if (totalParticipants == 0) {
            return toolType.getNiceName() + " event ended with no participants";
        }
        return toolType.getNiceName() + " event ended with " + totalParticipants + " participants. Winner achieved " + getWinningAmount();
    }

    @Override
    public boolean isCancellable() {
        return false;
    }
}
