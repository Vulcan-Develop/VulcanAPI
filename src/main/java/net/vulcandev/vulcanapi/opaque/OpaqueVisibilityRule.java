package net.vulcandev.vulcanapi.opaque;

import java.util.UUID;

/**
 * Keeps players visible to each other when Opaque would otherwise hide them, the way faction members
 * are, for parties, duels, teams or events. Register it with {@link OpaqueAPI#registerVisibilityRule}.
 *
 * <p>Opaque asks this off the server thread for every pair it checks, so an implementation must be
 * fast, thread-safe and must not call the Bukkit API; read from state you keep up to date yourself.
 * A rule never makes a player visible that Bukkit or another plugin hides.
 */
@FunctionalInterface
public interface OpaqueVisibilityRule {

    /** Whether {@code targetId} must always be shown to {@code viewerId}, whatever lies between them. */
    boolean alwaysVisible(UUID viewerId, UUID targetId);
}
