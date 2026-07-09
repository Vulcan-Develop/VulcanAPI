package net.vulcandev.vulcanapi.wrapper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@EqualsAndHashCode
public class EventStateWrapper {

    public enum State {
        WAITING,
        WARMUP,
        RUNNING,
        FINISHED
    }

    private final State state;

    public EventStateWrapper(@NotNull State state) {
        this.state = state;
    }

    @Nullable
    public static EventStateWrapper fromString(@NotNull String stateName) {
        try {
            return new EventStateWrapper(State.valueOf(stateName.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return state.name();
    }
}
