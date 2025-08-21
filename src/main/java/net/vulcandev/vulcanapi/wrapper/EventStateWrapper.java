package net.vulcandev.vulcanapi.wrapper;

import net.vulcandev.vulcanevents.enums.EventState;
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
     * Creates an EventStateWrapper from a VulcanEvents EventState
     * @param eventState the VulcanEvents EventState
     * @return EventStateWrapper instance or null if no matching state exists
     */
    @Nullable
    public static EventStateWrapper fromVulcanEventState(@NotNull EventState eventState) {
        try {
            String name = eventState.name();
            State wrapperState = State.valueOf(name);
            return new EventStateWrapper(wrapperState);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Converts this wrapper back to the original VulcanEvents EventState enum
     * @return the VulcanEvents EventState enum
     */
    @NotNull
    public EventState toVulcanEventState() {
        return EventState.valueOf(state.name());
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