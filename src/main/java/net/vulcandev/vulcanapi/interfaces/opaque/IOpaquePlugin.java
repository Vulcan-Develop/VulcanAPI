package net.vulcandev.vulcanapi.interfaces.opaque;

import net.vulcandev.vulcanapi.opaque.OpaqueBlockConcealment;
import net.vulcandev.vulcanapi.opaque.OpaqueStatistics;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

@ApiStatus.Internal
public interface IOpaquePlugin {

    boolean isConcealed(@NotNull UUID viewerId, @NotNull UUID targetId);

    @NotNull
    Set<UUID> getConcealedPlayers(@NotNull UUID viewerId);

    @NotNull
    OpaqueBlockConcealment getBlockConcealment(@NotNull UUID viewerId, @NotNull UUID worldId, int x, int y, int z);

    boolean isInsideBase(@NotNull UUID playerId);

    void reevaluate(@NotNull UUID viewerId);

    void reevaluateAll();

    @NotNull
    OpaqueStatistics getStatistics();
}
