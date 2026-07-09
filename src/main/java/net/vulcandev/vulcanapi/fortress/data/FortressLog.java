package net.vulcandev.vulcanapi.fortress.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public final class FortressLog {
    private final UUID id;
    private final UUID target;
    private final String checkName;
    private final String checkType;
    private final String checkDescription;
    private final int violation;
    private final int transactionPing;
    private final long loggedAt;
    private final List<String> data;

    public FortressLog(UUID id, UUID target, String checkName, String checkType, String checkDescription, int violation, int transactionPing, long loggedAt, List<String> data) {
        this.id = id;
        this.target = target;
        this.checkName = checkName;
        this.checkType = checkType;
        this.checkDescription = checkDescription;
        this.violation = violation;
        this.transactionPing = transactionPing;
        this.loggedAt = loggedAt;
        this.data = data == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<>(data));
    }
}
