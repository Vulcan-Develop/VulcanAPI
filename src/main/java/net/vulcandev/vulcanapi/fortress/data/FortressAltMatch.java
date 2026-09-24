package net.vulcandev.vulcanapi.fortress.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

@Getter
@AllArgsConstructor(onConstructor_ = @ApiStatus.Internal)
public final class FortressAltMatch {
    private final FortressPlayerSession session;
    private final boolean ipMatch;
    private final boolean sensitivityMatch;
    private final boolean brandModsMatch;
    private final int historyOverlapCount;
    private final int confidence;
    private final String matchLabel;
}
