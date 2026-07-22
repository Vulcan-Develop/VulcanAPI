package net.vulcandev.vulcanapi.replay.event;

public final class ReplayFailedEvent extends ReplayLifecycleEvent {
    private final String stage;
    private final String message;

    public ReplayFailedEvent(String clipId, String stage, String message) {
        super(clipId);
        this.stage = stage;
        this.message = message;
    }

    public String getStage() { return stage; }
    public String getMessage() { return message; }
}
