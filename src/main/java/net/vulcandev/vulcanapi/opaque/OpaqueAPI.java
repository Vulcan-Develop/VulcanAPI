package net.vulcandev.vulcanapi.opaque;

import net.vulcandev.vulcanapi.event.VulcanEventManager;
import net.vulcandev.vulcanapi.event.VulcanListener;
import net.vulcandev.vulcanapi.interfaces.opaque.IOpaquePlugin;
import net.vulcandev.vulcanapi.opaque.event.OpaquePlayerVisibilityEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;

/**
 * Reads what Opaque hides from each player, and lets other plugins keep players visible to each other
 * or exempt a player from Opaque. Every method is safe from any thread and answers with the default
 * (nothing hidden, no statistics) while Opaque is not running. Rules and exemptions are kept while
 * Opaque restarts and are dropped when the plugin that registered them disables.
 */
public final class OpaqueAPI {

    private static final Object LOCK = new Object();
    private static final RegisteredRule[] NO_RULES = new RegisteredRule[0];
    private static final Map<Plugin, List<OpaqueVisibilityRule>> RULES_BY_OWNER = new LinkedHashMap<>();
    private static final Map<UUID, Map<Plugin, Integer>> EXEMPTIONS_BY_PLAYER = new HashMap<>();
    private static final ConcurrentMap<UUID, Integer> EXEMPTION_MASKS = new ConcurrentHashMap<>();
    private static final int ENTITY_HIDING_BIT = 1 << OpaqueFeature.ENTITY_HIDING.ordinal();

    private static volatile RegisteredRule[] rules = NO_RULES;
    private static volatile Binding binding;

    private OpaqueAPI() {
    }

    public static boolean isAvailable() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Opaque");
        return plugin != null && plugin.isEnabled();
    }

    public static void registerListener(VulcanListener listener) {
        VulcanEventManager.getInstance().registerListener(listener);
    }

    public static void unregisterListener(VulcanListener listener) {
        VulcanEventManager.getInstance().unregisterListener(listener);
    }

    @ApiStatus.Internal
    public static void callEvent(OpaquePlayerVisibilityEvent event) {
        VulcanEventManager.getInstance().callEvent(event);
    }

    /**
     * Whether Opaque is hiding {@code targetId}'s player body from {@code viewerId} right now. A player
     * hidden by Bukkit or a vanish plugin is not counted; that is not Opaque's doing.
     */
    public static boolean isConcealed(UUID viewerId, UUID targetId) {
        IOpaquePlugin bridge = bridge();
        return bridge != null && viewerId != null && targetId != null && bridge.isConcealed(viewerId, targetId);
    }

    /** Whether Opaque is hiding {@code target}'s player body from {@code viewer} right now. */
    public static boolean isConcealed(Player viewer, Player target) {
        return viewer != null && target != null && isConcealed(viewer.getUniqueId(), target.getUniqueId());
    }

    /** Every online player whose body Opaque is hiding from {@code viewerId} right now. */
    public static Set<UUID> getConcealedPlayers(UUID viewerId) {
        IOpaquePlugin bridge = bridge();
        if (bridge == null || viewerId == null) return Collections.emptySet();
        return Collections.unmodifiableSet(bridge.getConcealedPlayers(viewerId));
    }

    /**
     * What Opaque is showing {@code viewerId} in place of the block at the given position, judged from
     * what that player's client was sent. {@link OpaqueBlockConcealment#NONE} for a block in another
     * world than the player's, or one Opaque does not protect.
     */
    public static OpaqueBlockConcealment getBlockConcealment(UUID viewerId, UUID worldId, int x, int y, int z) {
        IOpaquePlugin bridge = bridge();
        if (bridge == null || viewerId == null || worldId == null) return OpaqueBlockConcealment.NONE;
        return bridge.getBlockConcealment(viewerId, worldId, x, y, z);
    }

    /** What Opaque is showing {@code viewer} in place of {@code block}. */
    public static OpaqueBlockConcealment getBlockConcealment(Player viewer, Block block) {
        if (viewer == null || block == null) return OpaqueBlockConcealment.NONE;
        return getBlockConcealment(viewer.getUniqueId(), block.getWorld().getUID(),
                block.getX(), block.getY(), block.getZ());
    }

    /**
     * Whether the base hider counts the player as standing inside a sealed base room, as of its last
     * update. False while the base hider is off or bypassed for them.
     */
    public static boolean isInsideBase(UUID playerId) {
        IOpaquePlugin bridge = bridge();
        return bridge != null && playerId != null && bridge.isInsideBase(playerId);
    }

    /** What Opaque is concealing now and its totals since it enabled; all zero while it is not running. */
    public static OpaqueStatistics getStatistics() {
        IOpaquePlugin bridge = bridge();
        return bridge == null ? OpaqueStatistics.EMPTY : bridge.getStatistics();
    }

    /**
     * Checks the viewer against every player on the next tick instead of waiting for their turn. Call
     * it when one of your rules starts answering differently for that viewer, such as on joining a party.
     */
    public static void reevaluate(UUID viewerId) {
        IOpaquePlugin bridge = bridge();
        if (bridge != null && viewerId != null) bridge.reevaluate(viewerId);
    }

    /**
     * Adds a rule that keeps players visible to each other. Opaque shows a pair as soon as any
     * registered rule asks for it, and checks every player again on the next tick.
     */
    public static void registerVisibilityRule(Plugin owner, OpaqueVisibilityRule rule) {
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(rule, "rule");
        synchronized (LOCK) {
            List<OpaqueVisibilityRule> owned = RULES_BY_OWNER.get(owner);
            if (owned == null) {
                owned = new ArrayList<>(1);
                RULES_BY_OWNER.put(owner, owned);
            }
            for (OpaqueVisibilityRule registered : owned) if (registered == rule) return;
            owned.add(rule);
            publishRules();
        }
        reevaluateAll();
    }

    /** Removes a rule registered with {@link #registerVisibilityRule}. */
    public static void unregisterVisibilityRule(Plugin owner, OpaqueVisibilityRule rule) {
        if (owner == null || rule == null) return;
        synchronized (LOCK) {
            List<OpaqueVisibilityRule> owned = RULES_BY_OWNER.get(owner);
            if (owned == null || !owned.removeIf(registered -> registered == rule)) return;
            if (owned.isEmpty()) RULES_BY_OWNER.remove(owner);
            publishRules();
        }
        reevaluateAll();
    }

    /**
     * Exempts a player from part of Opaque, or ends that exemption, on behalf of {@code owner}. A player
     * is exempt while any plugin exempts them. An exempt player is treated the way a spectator is: for
     * {@link OpaqueFeature#ANTI_XRAY} that means chunks reach them unaltered and blocks already concealed
     * are revealed, and blocks sent while they were exempt stay as sent until their chunk is sent again.
     * Exemptions last until ended, or until {@code owner} disables, and survive the player reconnecting.
     */
    public static void setExempt(Plugin owner, UUID playerId, OpaqueFeature feature, boolean exempt) {
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(feature, "feature");
        int bit = 1 << feature.ordinal();
        int before;
        int after;
        synchronized (LOCK) {
            Map<Plugin, Integer> owners = EXEMPTIONS_BY_PLAYER.get(playerId);
            Integer held = owners == null ? null : owners.get(owner);
            int previous = held == null ? 0 : held;
            int next = exempt ? previous | bit : previous & ~bit;
            if (previous == next) return;
            if (owners == null) {
                owners = new HashMap<>(2);
                EXEMPTIONS_BY_PLAYER.put(playerId, owners);
            }
            if (next == 0) owners.remove(owner);
            else owners.put(owner, next);
            if (owners.isEmpty()) EXEMPTIONS_BY_PLAYER.remove(playerId);
            before = getExemptionMask(playerId);
            after = publishExemptions(playerId, owners);
        }
        if (((before ^ after) & ENTITY_HIDING_BIT) != 0) reevaluate(playerId);
    }

    /** Whether any plugin currently exempts the player from that part of Opaque. */
    public static boolean isExempt(UUID playerId, OpaqueFeature feature) {
        return feature != null && (getExemptionMask(playerId) & (1 << feature.ordinal())) != 0;
    }

    /** Removes every rule and exemption {@code owner} registered. Runs on its own when that plugin disables. */
    public static void release(Plugin owner) {
        if (owner == null) return;
        boolean rulesChanged;
        List<UUID> unhidden = new ArrayList<>();
        synchronized (LOCK) {
            rulesChanged = RULES_BY_OWNER.remove(owner) != null;
            if (rulesChanged) publishRules();
            Iterator<Map.Entry<UUID, Map<Plugin, Integer>>> players = EXEMPTIONS_BY_PLAYER.entrySet().iterator();
            while (players.hasNext()) {
                Map.Entry<UUID, Map<Plugin, Integer>> entry = players.next();
                if (entry.getValue().remove(owner) == null) continue;
                if (entry.getValue().isEmpty()) players.remove();
                int before = getExemptionMask(entry.getKey());
                int after = publishExemptions(entry.getKey(), entry.getValue());
                if (((before ^ after) & ENTITY_HIDING_BIT) != 0) unhidden.add(entry.getKey());
            }
        }
        if (rulesChanged) reevaluateAll();
        for (UUID playerId : unhidden) reevaluate(playerId);
    }

    @ApiStatus.Internal
    public static boolean isAlwaysVisible(UUID viewerId, UUID targetId) {
        for (RegisteredRule registered : rules) {
            if (registered.alwaysVisible(viewerId, targetId)) return true;
        }
        return false;
    }

    @ApiStatus.Internal
    public static int getExemptionMask(UUID playerId) {
        if (playerId == null || EXEMPTION_MASKS.isEmpty()) return 0;
        Integer mask = EXEMPTION_MASKS.get(playerId);
        return mask == null ? 0 : mask;
    }

    @ApiStatus.Internal
    public static void initialize(Plugin plugin, IOpaquePlugin bridge) {
        cleanup();
        if (plugin != null && bridge != null && "Opaque".equals(plugin.getName())) {
            binding = new Binding(plugin, bridge);
        }
    }

    @ApiStatus.Internal
    public static void cleanup() {
        binding = null;
    }

    private static IOpaquePlugin bridge() {
        Binding current = binding;
        return current != null && current.plugin.isEnabled() ? current.bridge : null;
    }

    private static void reevaluateAll() {
        IOpaquePlugin bridge = bridge();
        if (bridge != null) bridge.reevaluateAll();
    }

    private static void publishRules() {
        List<RegisteredRule> flat = new ArrayList<>();
        for (Map.Entry<Plugin, List<OpaqueVisibilityRule>> entry : RULES_BY_OWNER.entrySet()) {
            for (OpaqueVisibilityRule rule : entry.getValue()) flat.add(new RegisteredRule(entry.getKey(), rule));
        }
        rules = flat.isEmpty() ? NO_RULES : flat.toArray(new RegisteredRule[0]);
    }

    private static int publishExemptions(UUID playerId, Map<Plugin, Integer> owners) {
        int mask = 0;
        for (Integer owned : owners.values()) mask |= owned;
        if (mask == 0) EXEMPTION_MASKS.remove(playerId);
        else EXEMPTION_MASKS.put(playerId, mask);
        return mask;
    }

    private static final class Binding {
        private final Plugin plugin;
        private final IOpaquePlugin bridge;

        private Binding(Plugin plugin, IOpaquePlugin bridge) {
            this.plugin = plugin;
            this.bridge = bridge;
        }
    }

    private static final class RegisteredRule {
        private final Plugin owner;
        private final OpaqueVisibilityRule rule;
        private volatile boolean reported;

        private RegisteredRule(Plugin owner, OpaqueVisibilityRule rule) {
            this.owner = owner;
            this.rule = rule;
        }

        // A failing rule has no opinion; it is reported once rather than per pair.
        private boolean alwaysVisible(UUID viewerId, UUID targetId) {
            try {
                return rule.alwaysVisible(viewerId, targetId);
            } catch (RuntimeException | LinkageError failure) {
                if (!reported) {
                    reported = true;
                    owner.getLogger().log(Level.WARNING, "An Opaque visibility rule from " + owner.getName()
                            + " failed; Opaque ignores it until it stops failing", failure);
                }
                return false;
            }
        }
    }
}
