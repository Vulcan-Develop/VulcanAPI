package net.vulcandev.vulcanapi.vulcantools.wrapper;

import net.vulcandev.vulcantools.enums.ToolMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper for VulcanTools ToolMode to provide a clean API without exposing internal types
 */
public class ToolModeWrapper {
    
    public enum Mode {
        COLLECT,
        SELL,
        DEPOSIT,
        CUSTOM
    }
    
    private final Mode mode;
    private final String customName;
    
    public ToolModeWrapper(@NotNull Mode mode) {
        this.mode = mode;
        this.customName = null;
    }
    
    public ToolModeWrapper(@NotNull String customName) {
        this.mode = Mode.CUSTOM;
        this.customName = customName;
    }
    
    private ToolModeWrapper(@NotNull Mode mode, @Nullable String customName) {
        this.mode = mode;
        this.customName = customName;
    }
    
    /**
     * Gets the tool mode
     * @return the tool mode
     */
    @NotNull
    public Mode getMode() {
        return mode;
    }
    
    /**
     * Gets the custom name if this is a custom tool mode
     * @return the custom name or null if not a custom mode
     */
    @Nullable
    public String getCustomName() {
        return customName;
    }
    
    /**
     * Checks if this is a custom tool mode
     * @return true if custom, false otherwise
     */
    public boolean isCustom() {
        return mode == Mode.CUSTOM;
    }
    
    /**
     * Gets the next mode in the cycle
     * @return the next ToolModeWrapper
     */
    @NotNull
    public ToolModeWrapper nextMode() {
        switch (mode) {
            case COLLECT:
                return new ToolModeWrapper(Mode.SELL);
            case SELL:
                return new ToolModeWrapper(Mode.DEPOSIT);
            case DEPOSIT:
                return new ToolModeWrapper(Mode.COLLECT);
            default:
                return new ToolModeWrapper(Mode.SELL);
        }
    }
    
    /**
     * Creates a ToolModeWrapper from a VulcanTools ToolMode enum
     * @param toolMode the VulcanTools ToolMode enum
     * @return ToolModeWrapper instance
     */
    @NotNull
    public static ToolModeWrapper fromVulcanToolMode(@NotNull ToolMode toolMode) {
        try {
            String name = toolMode.name();
            Mode wrapperMode = Mode.valueOf(name);
            return new ToolModeWrapper(wrapperMode);
        } catch (IllegalArgumentException e) {
            // If the ToolMode doesn't match our enum, treat it as custom
            return new ToolModeWrapper(toolMode.name());
        }
    }
    
    /**
     * Creates a ToolModeWrapper from a string
     * @param modeName the tool mode name
     * @return ToolModeWrapper instance
     */
    @NotNull
    public static ToolModeWrapper fromString(@NotNull String modeName) {
        try {
            Mode mode = Mode.valueOf(modeName.toUpperCase());
            return new ToolModeWrapper(mode);
        } catch (IllegalArgumentException e) {
            return new ToolModeWrapper(modeName);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ToolModeWrapper that = (ToolModeWrapper) obj;
        
        if (mode != that.mode) return false;
        return customName != null ? customName.equals(that.customName) : that.customName == null;
    }
    
    @Override
    public int hashCode() {
        int result = mode.hashCode();
        result = 31 * result + (customName != null ? customName.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return customName != null ? customName : mode.name();
    }
}