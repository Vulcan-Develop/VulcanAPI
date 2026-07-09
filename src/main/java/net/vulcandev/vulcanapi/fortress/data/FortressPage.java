package net.vulcandev.vulcanapi.fortress.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public final class FortressPage<T> {
    private final List<T> rows;
    private final long totalRows;

    public FortressPage(List<T> rows, long totalRows) {
        this.rows = rows == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<>(rows));
        this.totalRows = Math.max(0L, totalRows);
    }
}
