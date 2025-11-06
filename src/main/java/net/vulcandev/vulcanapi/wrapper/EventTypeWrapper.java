package net.vulcandev.vulcanapi.wrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper for VulcanEvents EventType to provide a clean API without exposing internal types
 */
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

    /**
     * Gets the event type
     * @return the event type
     */
    @NotNull
    public Type getType() {
        return type;
    }

    /**
     * Creates an EventTypeWrapper from a string
     * @param typeName the event type name
     * @return EventTypeWrapper instance or null if no matching type exists
     */
    @Nullable
    public static EventTypeWrapper fromString(@NotNull String typeName) {
        try {
            Type type = Type.valueOf(typeName.toUpperCase());
            return new EventTypeWrapper(type);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        EventTypeWrapper that = (EventTypeWrapper) obj;
        
        return type == that.type;
    }
    
    @Override
    public int hashCode() {
        return type.hashCode();
    }
    
    @Override
    public String toString() {
        return type.name();
    }
}