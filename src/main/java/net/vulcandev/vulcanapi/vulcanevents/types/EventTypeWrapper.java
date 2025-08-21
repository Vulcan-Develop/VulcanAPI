package net.vulcandev.vulcanapi.vulcanevents.types;

import net.vulcandev.vulcanevents.enums.EventType;
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
        SKILLED_RACES,
        CUSTOM
    }
    
    private final Type type;
    private final String customName;
    
    public EventTypeWrapper(@NotNull Type type) {
        this.type = type;
        this.customName = null;
    }
    
    public EventTypeWrapper(@NotNull String customName) {
        this.type = Type.CUSTOM;
        this.customName = customName;
    }
    
    private EventTypeWrapper(@NotNull Type type, @Nullable String customName) {
        this.type = type;
        this.customName = customName;
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
     * Gets the custom name if this is a custom event type
     * @return the custom name or null if not a custom type
     */
    @Nullable
    public String getCustomName() {
        return customName;
    }
    
    /**
     * Checks if this is a custom event type
     * @return true if custom, false otherwise
     */
    public boolean isCustom() {
        return type == Type.CUSTOM;
    }
    
    /**
     * Creates an EventTypeWrapper from a VulcanEvents EventType
     * @param eventType the VulcanEvents EventType
     * @return EventTypeWrapper instance
     */
    @NotNull
    public static EventTypeWrapper fromVulcanEventType(@NotNull EventType eventType) {
        try {
            String name = eventType.name();
            Type wrapperType = Type.valueOf(name);
            return new EventTypeWrapper(wrapperType);
        } catch (IllegalArgumentException e) {
            // If the EventType doesn't match our enum, treat it as custom
            return new EventTypeWrapper(eventType.name());
        }
    }
    
    /**
     * Creates an EventTypeWrapper from a string
     * @param typeName the event type name
     * @return EventTypeWrapper instance
     */
    @NotNull
    public static EventTypeWrapper fromString(@NotNull String typeName) {
        try {
            Type type = Type.valueOf(typeName.toUpperCase());
            return new EventTypeWrapper(type);
        } catch (IllegalArgumentException e) {
            return new EventTypeWrapper(typeName);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        EventTypeWrapper that = (EventTypeWrapper) obj;
        
        if (type != that.type) return false;
        return customName != null ? customName.equals(that.customName) : that.customName == null;
    }
    
    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (customName != null ? customName.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return customName != null ? customName : type.name();
    }
}