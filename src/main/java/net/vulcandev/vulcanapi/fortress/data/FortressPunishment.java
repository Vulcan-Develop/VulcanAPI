package net.vulcandev.vulcanapi.fortress.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public final class FortressPunishment {
    private final UUID id;
    private final UUID target;
    private final String checkName;
    private final String checkType;
    private final long timestamp;
    private final String punishId;
    private final String issuer;
}
