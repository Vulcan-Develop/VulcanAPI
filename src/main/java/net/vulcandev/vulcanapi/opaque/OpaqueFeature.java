package net.vulcandev.vulcanapi.opaque;

/** The parts of Opaque a player can be exempted from with {@link OpaqueAPI#setExempt}. */
public enum OpaqueFeature {
    /** Hiding players, base mobs, storage carts and base holograms the player cannot see. */
    ENTITY_HIDING,
    /** Replacing ores, storage and other protected blocks the player cannot see. */
    ANTI_XRAY,
    /** Filling sealed base rooms the player is not near. */
    BASE_HIDER
}
