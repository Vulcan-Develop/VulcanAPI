package net.vulcandev.vulcanapi.opaque;

/** What, if anything, Opaque is showing a player in place of a block. */
public enum OpaqueBlockConcealment {
    /** The player's client holds the real block, or Opaque does not protect it. */
    NONE,
    /** Anti-xray is sending a replacement, such as stone in place of an ore the player cannot see. */
    ANTI_XRAY,
    /** The block lies in a sealed base room the base hider is sending as solid fill. */
    BASE_HIDER
}
