# VulcanAPI

VulcanAPI is the shared integration layer for Vulcan plugins. It gives other plugins one stable place to check which Vulcan modules are available, read supported data, and listen to public events.

## Current Release Focus

- Cleaner public APIs across VulcanEvents, VulcanStaff, VulcanTools, VulcanCrates, VulcanEnchants, VulcanGenBlocks, VulcanStats, VulcanVoting, and Fortress.
- Expanded Fortress reporting access for logs, punishments, player sessions, session snapshots, and alt-match results.
- Safer optional integration patterns so servers can run with only the Vulcan modules they use.
- Updated documentation for the current event systems and module support.

For the client-facing audit summary, see [CHANGELOG.txt](CHANGELOG.txt).

For Fortress anticheat integration details, see [ANTICHEAT.md](ANTICHEAT.md).

## Supported Modules

| Module | What VulcanAPI Exposes |
| --- | --- |
| VulcanEvents | Active event state, participants, spectators, event bans, and event status checks. |
| VulcanStaff | Vanish, staff mode, freeze state, and cancellable staff action events. |
| VulcanTools | Currency, booster, tool event managers, and tool event hooks. |
| VulcanCrates | Crate event hooks through the global Vulcan event bus. |
| VulcanEnchants | Enchant lookup, enchant lists, potion enchant metadata, item checks, and Bukkit enchant events. |
| VulcanGenBlocks | GenBlocks availability, plugin access, and gen bucket events. |
| VulcanStats | Player stats access when VulcanStats is loaded. |
| VulcanVoting | Voting availability and plugin access. |
| Fortress | Anticheat monitoring state, flag events, punish events, logs, punishments, sessions, and alt-match data. |

## Event Systems

VulcanAPI uses two event systems depending on the module.

Use Bukkit listeners for Bukkit events from VulcanEvents, VulcanStaff, VulcanEnchants, VulcanGenBlocks, and VulcanVoting.

Use `VulcanListener` with `net.vulcandev.vulcanapi.event.EventHandler` for `VulcanEvent` based events from Fortress, VulcanTools, and VulcanCrates.

```java
public final class ToolListener implements net.vulcandev.vulcanapi.event.VulcanListener {
    @net.vulcandev.vulcanapi.event.EventHandler
    public void onToolUpgrade(net.vulcandev.vulcanapi.vulcantools.events.ToolUpgradeEvent event) {
        if (event.isCancelled()) return;
    }
}
```

## Safe Integration

Use `softdepend: [VulcanLoader]` in `plugin.yml` and check availability before calling a module API.

Avoid importing optional Vulcan module API classes at the top of your main plugin class if your plugin must still load without VulcanAPI installed. Use fully qualified class names during startup checks.

```java
private void initializeVulcanApis() {
    org.bukkit.plugin.Plugin apiPlugin = getServer().getPluginManager().getPlugin("VulcanAPI");
    if (apiPlugin == null || !apiPlugin.isEnabled()) {
        getLogger().warning("VulcanAPI not found - optional Vulcan integrations disabled");
        return;
    }

    if (net.vulcandev.vulcanapi.vulcantools.VulcanToolsAPI.isAvailable()) {
        getLogger().info("VulcanTools integration enabled");
    }

    if (net.vulcandev.vulcanapi.fortress.FortressAPI.getInstance() != null) {
        getLogger().info("Fortress integration enabled");
    }
}
```

## Installation

VulcanAPI is compiled and distributed through the Vulcan Loader in the client panel at https://vulcandev.net/.

## Optional Dependencies

- VulcanEvents
- VulcanStaff
- VulcanTools
- VulcanCrates
- VulcanEnchants
- VulcanGenBlocks
- VulcanStats
- VulcanVoting
- Fortress

## Support

For support and questions, contact the development team.

Authors: Xanthard, OfficialGaming

Minecraft Version: 1.7 - Latest

## License

This project is proprietary software developed by VulcanDev.
