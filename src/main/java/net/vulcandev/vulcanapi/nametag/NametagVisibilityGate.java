package net.vulcandev.vulcanapi.nametag;

import java.util.UUID;

/**
 * A veto on rendering one viewer's nametag for one target.
 *
 * <p>Registered through {@link NametagAPI#registerVisibilityGate}, and consulted once per
 * viewer/target pair per update cycle. Returning false makes the nametag plugin destroy that pair's
 * armour stands and stop sending text for them; returning true again brings the tag straight back on
 * the next cycle, with no player spawn packet needed.</p>
 *
 * <p>This exists so a plugin that conceals players — an occlusion culler, a vanish system — can say
 * so directly rather than intercepting and mirroring the nametag plugin's fake entities. Suppressing
 * those packets from outside does not work: the sender records a stand as spawned the moment it
 * hands the packet over, so a cancelled spawn leaves it sending metadata for an entity the client
 * never received. {@link NametagAPI#forgetNametagEntity} is the escape hatch for the cases where a
 * marker packet is dropped anyway.</p>
 *
 * <h2>Contract</h2>
 * <ul>
 *   <li><b>Called off the server thread.</b> Gates run on the provider's nametag update thread.
 *       Implementations must not touch the Bukkit API; read from your own concurrent state.</li>
 *   <li><b>Called O(viewers × targets) per cycle</b> — every 500 ms by default. Keep it to a map
 *       lookup.</li>
 *   <li>A throwing gate is logged once and then treated as "render", so a broken integration
 *       cannot blank every nametag on the server.</li>
 * </ul>
 *
 * <p>Every registered gate must agree before a pair renders, and
 * {@link NametagAPI#setNametagHidden} can veto a target outright without one — the two are shapes of
 * the same mechanism, for a fact about a pair and a fact about a player respectively.</p>
 */
@FunctionalInterface
public interface NametagVisibilityGate {

    /**
     * @param viewer the player who would see the tag
     * @param target the player the tag is above
     * @return false to suppress this pair's nametag entirely
     */
    boolean shouldRender(UUID viewer, UUID target);
}
