package net.vulcandev.vulcanapi.wrapper;

import net.vulcandev.vulcantools.enums.ToolType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper for VulcanTools ToolType to provide a clean API without exposing internal types
 */
public class ToolTypeWrapper {
    
    public enum Type {
        FISHINGROD("Fishing Rod"),
        MOBSWORD("Mob Sword"),
        HARVESTERHOE("Harvester Hoe"),
        LUMBERAXE("Lumber Axe");
        
        private final String niceName;
        
        Type(String niceName) {
            this.niceName = niceName;
        }
    }
    
    private final Type type;
    
    public ToolTypeWrapper(@NotNull Type type) {
        this.type = type;
    }

    public String getNiceName() {return type.niceName;}
    
    /**
     * Gets the tool type
     * @return the tool type
     */
    @NotNull
    public Type getType() {
        return type;
    }
    
    /**
     * Creates a ToolTypeWrapper from a VulcanTools ToolType enum
     * @param toolType the VulcanTools ToolType enum
     * @return ToolTypeWrapper instance or null if no matching type exists
     */
    @Nullable
    public static ToolTypeWrapper fromVulcanToolType(@NotNull ToolType toolType) {
        try {
            String name = toolType.name();
            Type wrapperType = Type.valueOf(name);
            return new ToolTypeWrapper(wrapperType);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Converts this wrapper back to the original VulcanTools ToolType enum
     * @return the VulcanTools ToolType enum
     */
    @NotNull
    public ToolType toVulcanToolType() {
        return ToolType.valueOf(type.name());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ToolTypeWrapper that = (ToolTypeWrapper) obj;
        return type == that.type;
    }
    
    @Override
    public String toString() {
        return type.name();
    }
}