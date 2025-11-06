package net.vulcandev.vulcanapi.wrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper for VulcanEvents EventState to provide a clean API without exposing internal types
 */
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

    /**
     * Gets the event state
     * @return the event state
     */
    @NotNull
    public State getState() {
        return state;
    }

    /**
     * Creates an EventStateWrapper from a string
     * @param stateName the event state name
     * @return EventStateWrapper instance or null if no matching state exists
     */
    @Nullable
    public static EventStateWrapper fromString(@NotNull String stateName) {
        try {
            State state = State.valueOf(stateName.toUpperCase());
            return new EventStateWrapper(state);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        EventStateWrapper that = (EventStateWrapper) obj;

        return state == that.state;
    }
    
    @Override
    public String toString() {
        return state.name();
    }
}