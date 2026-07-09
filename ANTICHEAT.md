# Fortress Anticheat API

Fortress integration in VulcanAPI exposes supported anticheat data without requiring client plugins to depend on Fortress internals.

## What Is Exposed

- Live player profile and monitoring state.
- Fortress flag, punishment, alert, ghost block, crash attempt, player join, and player leave events.
- Async log and punishment lookups for audit panels, staff tools, and reporting dashboards.
- Player session lookups by UUID or name, full session snapshots, and alt-match results.
- Read-only data models for logs, punishments, player sessions, log statistics, paginated results, and alt matches.

## Event Access

Fortress events extend `VulcanEvent`, not Bukkit `Event`.

```java
public final class FortressListener implements net.vulcandev.vulcanapi.event.VulcanListener {
    @net.vulcandev.vulcanapi.event.EventHandler
    public void onFlag(net.vulcandev.vulcanapi.fortress.event.impl.PlayerFlagEvent event) {
        if (event.isCancelled()) return;
    }
}

net.vulcandev.vulcanapi.fortress.FortressAPI fortress = net.vulcandev.vulcanapi.fortress.FortressAPI.getInstance();

if (fortress != null && fortress.isEnabled()) {
    fortress.registerListener(new FortressListener());
}
```

## Audit Access

The new audit methods return `CompletableFuture` values so integrations can request Fortress data without blocking the server thread.

```java
net.vulcandev.vulcanapi.fortress.FortressAPI fortress =
        net.vulcandev.vulcanapi.fortress.FortressAPI.getInstance();

if (fortress != null && fortress.isDatabaseReady()) {
    fortress.getLogs(playerUuid).thenAccept(logs -> {
        // Update your panel, report, or staff view.
    });

    fortress.getLogStats(System.currentTimeMillis() - 86400000L, 10, 10).thenAccept(stats -> {
        long totalFlags = stats.getTotalFlags();
    });
}
```

## Available Audit Methods

- `isDatabaseReady()`
- `getLogs(UUID uuid)`
- `getLogsSince(long sinceTimestamp)`
- `getLogsPage(long since, long until, UUID target, String checkName, String checkType, int offset, int limit)`
- `getLogStats(long since, int topChecks, int topPlayers)`
- `getPunishments(UUID uuid)`
- `getPunishmentsSince(long sinceTimestamp)`
- `getPunishmentsSince(long sinceTimestamp, String checkName, String checkType)`
- `getPunishmentsPage(long since, long until, UUID target, int offset, int limit)`
- `countPunishmentsSince(long sinceTimestamp)`
- `getSession(UUID uuid)`
- `getSessionByName(String name)`
- `getSessions(Collection<UUID> uuids)`
- `getSessionSnapshot()`
- `getAltMatches(UUID uuid)`

Default implementations return safe empty results when a Fortress implementation does not support the requested data.
