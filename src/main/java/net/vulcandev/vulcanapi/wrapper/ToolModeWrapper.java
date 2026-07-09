package net.vulcandev.vulcanapi.wrapper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.vulcandev.vulcantools.enums.ToolMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@EqualsAndHashCode
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

    @Nullable
    public static ToolModeWrapper fromVulcanToolMode(@NotNull ToolMode toolMode) {
        try {
            return new ToolModeWrapper(Mode.valueOf(toolMode.name()));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @NotNull
    public ToolMode toVulcanToolMode() {
        return ToolMode.valueOf(mode.name());
    }

    @Override
    public String toString() {
        return mode.name();
    }
}
