package net.vulcandev.vulcanapi.wrapper;

import net.vulcandev.genblocks.managers.BucketManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper for VulcanGenBlocks GenDirection to provide a clean API without exposing internal types
 */
public class GenDirectionWrapper {

    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        UP,
        DOWN
    }

    private final Direction direction;

    public GenDirectionWrapper(@NotNull Direction direction) {
        this.direction = direction;
    }

    @NotNull
    public Direction getDirection() {
        return direction;
    }

    @Nullable
    public static GenDirectionWrapper fromVulcanGenDirection(@Nullable BucketManager.Direction genDirection) {
        if (genDirection == null) return null;
        try {
            Direction wrapperDirection = Direction.valueOf(genDirection.name());
            return new GenDirectionWrapper(wrapperDirection);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @NotNull
    public BucketManager.Direction toVulcanGenDirection() {
        return BucketManager.Direction.valueOf(direction.name());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GenDirectionWrapper that = (GenDirectionWrapper) obj;
        return direction == that.direction;
    }

    @Override
    public int hashCode() {
        return direction.hashCode();
    }

    @Override
    public String toString() {
        return direction.name();
    }
}