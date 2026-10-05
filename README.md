# VulcanAPI

VulcanAPI is the shared integration layer for Vulcan plugins. It gives other plugins one stable place to check which Vulcan modules are available, read supported data, and listen to public events.

## Current Release Focus

- Cleaner public APIs across VulcanEvents, VulcanStaff, VulcanTools, VulcanCrates, VulcanEnchants, VulcanGenBlocks, VulcanStats, VulcanVoting, and Fortress.
- Expanded Fortress reporting access for logs, punishments, player sessions, session snapshots, and alt-match results.
- Safer optional integration patterns so servers can run with only the Vulcan modules they use.
- Updated documentation for the current event systems and module support.
- Added Opaque conceal/reveal events and synchronous nametag-retention claims.

For the client-facing audit summary, see [CHANGELOG.txt](CHANGELOG.txt).

For Fortress anticheat integration details, see the Fortress module in the
[API reference](https://vulcandev.net/docs/api).

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
| VulcanReplay | Rolling-buffer inspection, replay saves, markers, clip queries, and replay lifecycle events. |
| Fortress | Anticheat monitoring state, flag events, punish events, logs, punishments, sessions, and alt-match data. |
| Opaque | Per-viewer player conceal and reveal events with nametag ownership handoff, who is hidden from whom, which blocks a player is being shown a replacement for, whether a player is inside a sealed base, visibility rules that keep allies visible, per-player exemptions, and statistics. |
| VulcanClicker | Armed and clicking state for the left, right and inventory clickers, arming control, per-player CPS overrides, whitelist checks, global enable switches, per-player blocks, and arm and hold events. |

## Event Systems

VulcanAPI uses two event systems depending on the module.

Use Bukkit listeners for Bukkit events from VulcanEvents, VulcanStaff, VulcanEnchants, VulcanGenBlocks, and VulcanVoting.

Use `VulcanListener` with `net.vulcandev.vulcanapi.event.EventHandler` for `VulcanEvent` based events from Fortress, VulcanTools, VulcanCrates, Opaque, and VulcanClicker.

```java
public final class ToolListener implements net.vulcandev.vulcanapi.event.VulcanListener {
    @net.vulcandev.vulcanapi.event.EventHandler(
            priority = net.vulcandev.vulcanapi.event.EventPriority.MONITOR,
            ignoreCancelled = true)
    public void onToolUpgrade(net.vulcandev.vulcanapi.vulcantools.events.ToolUpgradeEvent event) {
    }
}
```

Vulcan events run from `LOWEST` through `MONITOR`. Cancelled events continue through the bus, but
handlers with `ignoreCancelled = true` are skipped. `MONITOR` is observation-only.

`OpaquePlayerVisibilityEvent` is synchronous and may run on a player's network event loop. It uses
UUIDs rather than Bukkit players. A nametag renderer that owns an independently tracked marker may
call `retainNametag()` during `CONCEALED`; `VISIBLE` is notification-only.

## Opaque

`OpaqueAPI` is static and safe from any thread. While Opaque is not running, queries answer with
nothing hidden and the statistics are all zero.

```java
// Party members always see each other, the way faction members do. Called off the server thread
// for every pair Opaque checks, so it reads only state the plugin keeps itself.
OpaqueAPI.registerVisibilityRule(plugin, (viewer, target) -> parties.sameParty(viewer, target));
OpaqueAPI.reevaluate(player.getUniqueId()); // after the player joins or leaves a party

// A staff member reviewing an x-ray report sees ores as they really are.
OpaqueAPI.setExempt(plugin, staff.getUniqueId(), OpaqueFeature.ANTI_XRAY, true);

// Was the player being sent stone where this diamond is when they started digging towards it?
boolean hidden = OpaqueAPI.getBlockConcealment(player, block) == OpaqueBlockConcealment.ANTI_XRAY;

// Is the player tracking someone Opaque is hiding from them? Done repeatedly, that points to ESP.
boolean watchingHidden = OpaqueAPI.isConcealed(player, target);
```

Rules and exemptions are dropped when the plugin that registered them disables. A rule never shows a
player that Bukkit or a vanish plugin hides.

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
- Opaque
- Fortress
- VulcanClicker

## Support

For support and questions, contact the development team.

Authors: Xanthard, OfficialGaming

Minecraft Version: 1.7 - Latest

## License

This project is proprietary software developed by VulcanDev.
