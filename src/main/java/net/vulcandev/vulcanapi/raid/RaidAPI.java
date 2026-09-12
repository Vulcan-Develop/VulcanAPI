package net.vulcandev.vulcanapi.raid;

import net.vulcandev.vulcanapi.event.VulcanEventManager;
import net.vulcandev.vulcanapi.event.VulcanListener;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public interface RaidAPI {
    boolean isEnabled();

    RaidNodeView getNode(UUID id);

    Collection<RaidNodeView> getActiveNodes();

    Collection<RaidNodeView> getNodes(RaidQuery query);

    RaidStatsView getStats(UUID id);

    default RaidNodeView startRaid(String attackingFactionId, String defendingFactionId,
                                   RaidLocation source, RaidLocation impact) {
        return null;
    }

    default RaidNodeView endRaid(UUID id, RaidEndReason reason) {
        return null;
    }

    default void registerListener(VulcanListener listener) {
        VulcanEventManager.getInstance().registerListener(listener);
    }

    default void unregisterListener(VulcanListener listener) {
        VulcanEventManager.getInstance().unregisterListener(listener);
    }

    static RaidAPI getInstance() {
        return RaidAPIInstance.getInstance();
    }

    final class RaidAPIInstance {
        private static volatile RaidAPI instance = new Empty();

        private RaidAPIInstance() {
        }

        public static RaidAPI getInstance() {
            return instance;
        }

        public static void setInstance(RaidAPI value) {
            instance = value;
        }
    }

    final class Empty implements RaidAPI {
        @Override
        public boolean isEnabled() { return false; }

        @Override
        public RaidNodeView getNode(UUID id) { return null; }

        @Override
        public Collection<RaidNodeView> getActiveNodes() { return Collections.emptyList(); }

        @Override
        public Collection<RaidNodeView> getNodes(RaidQuery query) { return Collections.emptyList(); }

        @Override
        public RaidStatsView getStats(UUID id) { return new RaidStatsView(0, 0, 0, 0, 0, 0, 0D, 0D); }
    }
}
