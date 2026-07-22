package net.vulcandev.vulcanapi.replay.event;

public final class ReplayPressureEvent extends ReplayLifecycleEvent {
    private final String lane;
    private final int queued;
    private final int capacity;

    public ReplayPressureEvent(String lane, int queued, int capacity) {
        super(null);
        this.lane = lane;
        this.queued = queued;
        this.capacity = capacity;
    }

    public String getLane() { return lane; }
    public int getQueued() { return queued; }
    public int getCapacity() { return capacity; }
}
