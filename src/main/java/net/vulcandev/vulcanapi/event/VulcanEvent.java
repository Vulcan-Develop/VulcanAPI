package net.vulcandev.vulcanapi.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class VulcanEvent {
    private boolean cancelled = false;
    private final long timestamp = System.currentTimeMillis();

    public abstract boolean isCancellable();
}
