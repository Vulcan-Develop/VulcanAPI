package net.vulcandev.vulcanapi.wrapper;

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
        DEPOSIT
    }
    
    private final Mode mode;
    
    public ToolModeWrapper(@NotNull Mode mode) {
        this.mode = mode;
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
     * @return ToolModeWrapper instance or null if no matching mode exists
     */
    @Nullable
    public static ToolModeWrapper fromVulcanToolMode(@NotNull ToolMode toolMode) {
        try {
            String name = toolMode.name();
            Mode wrapperMode = Mode.valueOf(name);
            return new ToolModeWrapper(wrapperMode);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Converts this wrapper back to the original VulcanTools ToolMode enum
     * @return the VulcanTools ToolMode enum
     */
    @NotNull
    public ToolMode toVulcanToolMode() {
        return ToolMode.valueOf(mode.name());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ToolModeWrapper that = (ToolModeWrapper) obj;
        
        return mode == that.mode;
    }
    
    @Override
    public int hashCode() {
        return mode.hashCode();
    }
    
    @Override
    public String toString() {
        return mode.name();
    }
}