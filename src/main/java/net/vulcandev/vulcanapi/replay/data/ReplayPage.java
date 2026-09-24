package net.vulcandev.vulcanapi.replay.data;

import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.List;

public final class ReplayPage<T> {
    private final List<T> items;
    private final long total;

    @ApiStatus.Internal
    public ReplayPage(List<T> items, long total) {
        this.items = Collections.unmodifiableList(items);
        this.total = total;
    }

    public List<T> getItems() { return items; }
    public long getTotal() { return total; }
}
