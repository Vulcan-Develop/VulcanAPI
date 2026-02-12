package net.vulcandev.vulcanapi.event;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean cancelled);
}
