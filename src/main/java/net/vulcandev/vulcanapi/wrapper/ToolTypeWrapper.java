package net.vulcandev.vulcanapi.wrapper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.vulcandev.vulcantools.enums.ToolType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@EqualsAndHashCode
public class ToolTypeWrapper {

    @Getter
    public enum Type {
        FISHINGROD("Fishing Rod"),
        MOBSWORD("Mob Sword"),
        HARVESTERHOE("Harvester Hoe"),
        SHOVEL("Shovel"),
        LUMBERAXE("Lumber Axe"),
        MINERPICKAXE("Miner Pickaxe");
        
        private final String niceName;
        
        Type(String niceName) {
            this.niceName = niceName;
        }
    }
    
    private final Type type;
    
    public ToolTypeWrapper(@NotNull Type type) {
        this.type = type;
    }

    public String getNiceName() {
        return type.niceName;
    }

    @Nullable
    public static ToolTypeWrapper fromVulcanToolType(@NotNull ToolType toolType) {
        try {
            return new ToolTypeWrapper(Type.valueOf(toolType.name()));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @NotNull
    public ToolType toVulcanToolType() {
        return ToolType.valueOf(type.name());
    }

    @Override
    public String toString() {
        return type.name();
    }
}
