package net.vulcandev.vulcanapi.fortress.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public final class FortressPlayerSession {
    private final UUID uuid;
    private final String name;
    private final String clientVersion;
    private final String clientBrand;
    private final boolean bedrock;
    private final boolean lunarClient;
    private final int sensitivity;
    private final long lastSeen;
    private final String ipAddress;
    private final String modsLoaded;
}
