package net.vulcandev.vulcanapi.wrapper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.vulcandev.genblocks.managers.BucketManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@EqualsAndHashCode
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
    public String toString() {
        return direction.name();
    }
}
