package net.vulcandev.vulcanapi.nametag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

/**
 * The contract between whichever plugin draws nametags and the plugins that need to influence them.
 *
 * <p>Implemented by the nametag provider — VulcanTab today — and registered with Bukkit's services
 * manager. Consume it from there rather than reaching for a plugin instance, so your plugin still
 * loads when no provider is installed:</p>
 *
 * <pre>{@code
 * NametagAPI api = NametagAPI.get();
 * if (api != null) api.registerVisibilityGate(myGate);
 * }</pre>
 *
 * <p>The service is registered during the provider's enable, so resolve it lazily or re-resolve on
 * {@code PluginEnableEvent} — a plugin that enables first would otherwise cache null forever.</p>
 *
 * <p>Everything here is safe to call on the server thread. Only {@link NametagVisibilityGate}
 * callbacks run elsewhere; see that interface for its threading contract.</p>
 */
public interface NametagAPI {

    /** The registered provider, or null when none is installed or it has not enabled yet. */
    static NametagAPI get() {
        RegisteredServiceProvider<NametagAPI> provider =
                Bukkit.getServicesManager().getRegistration(NametagAPI.class);
        return provider == null ? null : provider.getProvider();
    }

    /** The plugin providing the registered service, or null when there is none. */
    static org.bukkit.plugin.Plugin provider() {
        RegisteredServiceProvider<NametagAPI> provider =
                Bukkit.getServicesManager().getRegistration(NametagAPI.class);
        return provider == null ? null : provider.getPlugin();
    }

    // ------------------------------------------------------------------ refresh

    /**
     * Rebuild every viewer's nametag for one target, now.
     * <p>
     * The call for "this player's own data changed" — a rank change, a disguise, a nickname.
     * Ordinary changes are picked up by the update sweep within one interval anyway; this is for
     * when that delay is visible.
     */
    void refreshPlayer(Player target);

    /** Rebuild one viewer's view of every target, now. For a change to the viewer's own style. */
    void refreshViewer(Player viewer);

    /**
     * Rebuild exactly one viewer/target pair, now.
     * <p>
     * Pair with a {@link NametagVisibilityGate} flip so the tag appears with the player instead of
     * up to one update interval later.
     */
    void refreshPair(Player viewer, Player target);

    // ------------------------------------------------- marker entity identification

    /**
     * Whether unlimited nametag mode is actually running.
     * <p>
     * Reads the effective state, not the config key: the mode is refused when the protocol backend
     * has no verified metadata layout for this server version. Informational only — marker lookups
     * answer from the id index and stay correct either way.
     */
    boolean unlimitedModeActive();

    /**
     * Whether an entity id is one of the provider's invisible nametag markers for this viewer.
     * <p>
     * True from the moment the id is allocated, which is before its spawn packet goes out, and it
     * stays true while the stand is untracked. A packet filter can therefore recognise every marker
     * packet it will ever see, including the spawn.
     */
    boolean isNametagEntity(UUID viewer, int entityId);

    /**
     * The player a marker entity id is describing, or null when the id is not the provider's.
     * <p>
     * Exact, unlike inferring the pairing from the stand's position — which cannot separate two
     * players standing closer together than the tag offset.
     */
    UUID nametagTarget(UUID viewer, int entityId);

    /**
     * Tell the provider that one of its marker entities never reached the client, so it re-sends it.
     *
     * <p><b>Call this whenever you drop, cancel or destroy a marker packet you did not send.</b> A
     * nametag sender records a stand as spawned the moment it hands the packet over — it has no
     * other signal — so a spawn that is suppressed downstream leaves it believing a stand is live
     * forever and sending metadata for an entity the client never received. Nothing on either side
     * re-checks, and the target is left with no nametag permanently. This clears that one line's
     * spawn state so the next update sweep re-spawns it at the same entity id.</p>
     *
     * <p>Preferring {@link NametagVisibilityGate} is still the right design — it means the packet is
     * never sent in the first place. This is for the cases the gate cannot cover: a marker already
     * in flight when concealment begins, a batch destroy that swept one up, a window where the
     * provider was reloading. Cheap and idempotent; calling it for an id that is not a marker does
     * nothing.</p>
     *
     * @param viewer   the player whose client lost the entity
     * @param entityId the marker entity id that was dropped
     */
    void forgetNametagEntity(UUID viewer, int entityId);

    // ---------------------------------------------------------- per-target hiding

    /**
     * Hide or restore one player's nametag for everyone at once.
     * <p>
     * The entry point for a state that belongs to the <b>target</b> and changes rarely — a
     * disguise, say. {@link NametagVisibilityGate} is the other half of the same mechanism, for a
     * decision that belongs to a viewer/target <b>pair</b> and is re-asked every cycle; use
     * whichever matches the shape of what you know. A player is hidden if either says so.
     * <p>
     * Repaints immediately, so there is no need to follow it with {@link #refreshPlayer}.
     * <p>
     * <b>Cleared when the player quits.</b> Re-apply on join if the state should outlive the
     * session — the provider does not know why you hid them and will not guess.
     *
     * @param target the player whose tag to hide, online or not
     */
    void setNametagHidden(UUID target, boolean hidden);

    /** Whether {@link #setNametagHidden} currently hides this player. Ignores registered gates. */
    boolean isNametagHidden(UUID target);

    // ------------------------------------------------------------ visibility gate

    /**
     * Add a veto on rendering individual viewer/target pairs. Idempotent per instance.
     * <p>
     * Registering does not by itself repaint anything; call {@link #refreshPair} when a decision
     * changes.
     */
    void registerVisibilityGate(NametagVisibilityGate gate);

    /** Remove a gate. Every pair it was suppressing renders again on the next cycle. */
    void unregisterVisibilityGate(NametagVisibilityGate gate);
}
