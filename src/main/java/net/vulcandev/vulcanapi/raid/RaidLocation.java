package net.vulcandev.vulcanapi.raid;

import lombok.Getter;

@Getter
public final class RaidLocation {
    private final String world;
    private final int x;
    private final int y;
    private final int z;

    public RaidLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
