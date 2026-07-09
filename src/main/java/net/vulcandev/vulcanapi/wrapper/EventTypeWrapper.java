package net.vulcandev.vulcanapi.wrapper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@EqualsAndHashCode
public class EventTypeWrapper {

    public enum Type {
        LMS,
        SPLEEF,
        TNT_TAG,
        MICRO_BATTLES,
        PARKOUR,
        TOWER_WARS,
        ONE_IN_THE_CHAMBER,
        SKILLED_RACES
    }

    private final Type type;

    public EventTypeWrapper(@NotNull Type type) {
        this.type = type;
    }

    @Nullable
    public static EventTypeWrapper fromString(@NotNull String typeName) {
        try {
            return new EventTypeWrapper(Type.valueOf(typeName.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return type.name();
    }
}
