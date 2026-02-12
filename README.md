# VulcanAPI

API for VulcanEvents, VulcanStaff, VulcanTools, VulcanEnchants, and Fortress.

## Global Event System

VulcanAPI provides a unified, global event system that all Vulcan plugins can use for cross-plugin communication and custom event handling.

### Core Event Framework

All events extend `VulcanEvent` and can be listened to by any plugin implementing `VulcanListener`.

**Event Classes:**
- `VulcanEvent` - Base class for all events
- `VulcanListener` - Interface for event listeners
- `VulcanEventManager` - Global singleton event bus
- `EventHandler` - Annotation to mark listener methods
- `EventPriority` - Event priority levels (LOWEST, LOW, NORMAL, HIGH, HIGHEST, MONITOR)
- `Cancellable` - Interface for cancellable events

### Quick Start

**Creating a Custom Event:**
```java
public class MyCustomEvent extends VulcanEvent {
    private final Player player;
    private final String message;

    public MyCustomEvent(Player player, String message) {
        this.player = player;
        this.message = message;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }

    public Player getPlayer() { return player; }
    public String getMessage() { return message; }
}
```

**Creating a Listener:**
```java
public class MyListener implements VulcanListener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onMyEvent(MyCustomEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("Event received: " + event.getMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerFlag(PlayerFlagEvent event) {
        // Listen to Fortress events from any plugin
        String checkName = event.getCheckName();
    }
}
```

**Registering & Firing Events:**
```java
import net.vulcandev.vulcanapi.event.*;

// Get the global event manager
VulcanEventManager eventManager = VulcanEventManager.getInstance();

// Register a listener
eventManager.registerListener(new MyListener());

// Fire an event
MyCustomEvent event = new MyCustomEvent(player, "Hello World");
boolean cancelled = eventManager.callEvent(event);

// Unregister when done
eventManager.unregisterListener(listener);
```

**Optional Logging:**
```java
// Set a custom logger for event system messages
VulcanEventManager.getInstance().setLogger(msg -> {
    Bukkit.getLogger().info("[Events] " + msg);
});
```

### Event Priorities

Events are processed in priority order (highest to lowest):

- `MONITOR` (5) - Final observation, cannot cancel events
- `HIGHEST` (4) - Last chance to modify
- `HIGH` (3) - High priority modifications
- `NORMAL` (2) - Default priority
- `LOW` (1) - Early modifications
- `LOWEST` (0) - First to process

### Cross-Plugin Communication

Any Vulcan plugin can listen to events from other plugins:

```java
@EventHandler
public void onEnchantApply(CEApplyEvent event) {
    // VulcanStaff listening to VulcanEnchants
}

@EventHandler
public void onFortressFlag(PlayerFlagEvent event) {
    // VulcanTools listening to Fortress
    if (event.getCheckName().equals("KillAura")) {
        // React to anticheat flags
    }
}
```

---

## Fortress API

Fortress anticheat integration for flag monitoring, player tracking, and check management.

> **Note:** Fortress events are **VulcanEvents** (custom event system, `extends VulcanEvent`). Use `VulcanEventManager` to listen to them as shown in the Global Event System section above. Do NOT use Bukkit's `@EventHandler` or `org.bukkit.event.Listener` for these events.

### Basic Usage

```java
import net.vulcandev.vulcanapi.fortress.FortressAPI;
import net.vulcandev.vulcanapi.fortress.player.PlayerProfile;

FortressAPI fortress = FortressAPI.getInstance();

// Get player profile
PlayerProfile profile = fortress.getPlayerProfile(player.getUniqueId());
if (profile != null) {
    String clientName = profile.getClientName();
    String version = profile.getVersion();
    long ping = profile.getPing();
}

// Check monitoring status
boolean monitored = fortress.isPlayerMonitored(uuid);

// Get all online profiles
Collection<PlayerProfile> profiles = fortress.getOnlineProfiles();
```

### Available Fortress Events

**Flag Events:**
```java
@EventHandler
public void onPlayerFlag(PlayerFlagEvent event) {
    PlayerProfile player = event.getPlayer();
    CheckType checkName = event.getCheckName();
    String checkType = event.getCheckType();
    int violations = event.getViolations();

    if (event.isCancelled()) return;
    event.setCancelled(true); // Prevent flag from processing
}
```

**Alert Events:**
```java
@EventHandler
public void onAlertToggle(AlertToggleEvent event) {
    UUID staff = event.getUuid();
    boolean enabled = event.isEnabled();
}
```

**Punishment Events:**
```java
@EventHandler
public void onPlayerPunish(PlayerPunishEvent event) {
    UUID player = event.getUuid();
    String punishment = event.getPunishmentType();
    String command = event.getCommand();
}
```

**Banwave Events:**
```java
@EventHandler
public void onBanwave(BanwaveEvent event) {
    List<UUID> players = event.getPlayers();
    event.setCancelled(true); // Cancel banwave
}
```

**Additional Events:**
- `PlayerJoinEvent` - Player joins and starts being monitored
- `PlayerLeaveEvent` - Player leaves and stops being monitored
- `PlayerKickEvent` - Player kicked by anticheat
- `AurabotSpawnEvent` / `AurabotDespawnEvent` - Aurabot management
- `GhostBlockEvent` - Ghost block placement/removal
- `AttemptedCrashEvent` - Crash attempt detection

### Check Types

Access check information through events:

```java
CheckType checkType = event.getCheckName();
String checkVariant = event.getCheckType(); // A, B, C, etc.
String advanced = event.getCheckTypeAdvanced(); // Detailed variant info
```

Available categories:
- Combat: KillAura, Reach, Velocity, etc.
- Movement: Fly, Speed, Step, etc.
- Player: BadPackets, Timer, Inventory, etc.
- World: Scaffold, FastPlace, etc.

---

## Current API Features

- **VulcanEventsAPI**: Manage server events, competitions, and player participation
- **VulcanStaffAPI**: Staff management tools including vanish, staff mode, and freeze functionality
- **VulcanToolsAPI**: Advanced tool system with currency management, boosters, and tool events
- **VulcanEnchantsAPI**: Custom enchantments system with potion effects and special abilities
- **FortressAPI**: Anticheat integration for flag monitoring and player tracking
- **Global Event System**: Unified event bus for cross-plugin communication

## Integration Guide

### Initialization (Recommended Pattern)

Use this method in your plugin's `onEnable()`:

> **⚠️ Important**: Do not add imports for the API classes at the top of your main plugin class (e.g., `import net.vulcandev.vulcanapi.vulcanevents.VulcanEventsAPI;`). If VulcanAPI is not present on the server, Java will fail to load your plugin class entirely, throwing a `NoClassDefFoundError` during plugin initialization. Instead, use fully qualified class names as shown below, which allows your plugin to load even when VulcanAPI is absent.

```java
private void initializeVulcanAPIs() {
    Plugin vulcanAPI = getServer().getPluginManager().getPlugin("VulcanAPI");
    if (vulcanAPI == null || !vulcanAPI.isEnabled()) {
        getLogger().warning("VulcanAPI not found - API features disabled");
        return;
    }

    if (net.vulcandev.vulcanapi.vulcanevents.VulcanEventsAPI.isAvailable()) {
        getLogger().info("VulcanEventsAPI enabled");
    }

    if (net.vulcandev.vulcanapi.vulcanstaff.VulcanStaffAPI.isAvailable()) {
        getLogger().info("VulcanStaffAPI enabled");
    }

    if (net.vulcandev.vulcanapi.vulcantools.VulcanToolsAPI.isAvailable()) {
        getLogger().info("VulcanToolsAPI enabled");
    }

    if (net.vulcandev.vulcanapi.vulcanenchants.VulcanEnchantsAPI.isAvailable()) {
        getLogger().info("VulcanEnchantsAPI enabled");
    }

    if (net.vulcandev.vulcanapi.fortress.FortressAPI.getInstance() != null) {
        getLogger().info("FortressAPI enabled");
    }
}
```

### Plugin Dependencies (plugin.yml)

```yaml
name: YourPlugin
main: com.yourplugin.Main
version: 1.0.0
softdepend: [VulcanLoader]
```

## API Usage

### 1. VulcanEventsAPI

Manage server events and player participation.

> **Note:** These are **Bukkit Events** (`extends org.bukkit.event.Event`). Use Bukkit's event system to listen to them:
> ```java
> public class MyListener implements org.bukkit.event.Listener {
>     @org.bukkit.event.EventHandler
>     public void onEventStart(EventStartEvent event) {
>         // Handle Bukkit event
>     }
> }
>
> // Register with Bukkit
> Bukkit.getPluginManager().registerEvents(new MyListener(), plugin);
> ```

**Basic Usage:**
```java
if (VulcanEventsAPI.isAvailable()) {
    VulcanEventsAPI api = VulcanEventsAPI.getInstance();

    if (api.hasActiveEvent()) {
        String name = api.getCurrentEventName();
        int participants = api.getParticipantCount();
    }
}
```

**Key Methods:**
```java
boolean hasActiveEvent()
String getCurrentEventName()
EventState getCurrentEventState()
int getTimeRemaining()

boolean isPlayerInEvent(Player player)
boolean isPlayerSpectating(Player player)
int getParticipantCount()
int getSpectatorCount()
```

**Available Events:**
- EventStartEvent, EventEndEvent, EventStateChangeEvent
- PlayerJoinEventEvent, PlayerLeaveEventEvent, PlayerEliminateEvent
- PlayerSpectateEventEvent, KitPreApplyEvent, KitPostApplyEvent

---

### 2. VulcanStaffAPI

Manage staff features including vanish, staff mode, and player freezing.

> **Note:** These are **Bukkit Events** (`extends org.bukkit.event.Event`). Use Bukkit's event system to listen to them:
> ```java
> public class MyListener implements org.bukkit.event.Listener {
>     @org.bukkit.event.EventHandler
>     public void onStaffVanish(StaffVanishEvent event) {
>         // Handle Bukkit event
>     }
> }
>
> // Register with Bukkit
> Bukkit.getPluginManager().registerEvents(new MyListener(), plugin);
> ```

**Basic Usage:**
```java
if (VulcanStaffAPI.isAvailable()) {
    VulcanStaffAPI api = VulcanStaffAPI.getInstance();

    api.setVanished(player, true);
    boolean frozen = api.isFrozen(player);
}
```

**Key Methods:**
```java
boolean isVanished(Player player)
boolean setVanished(Player player, boolean vanished)
Set<UUID> getVanishedPlayers()

boolean isInStaffMode(Player player)

boolean isFrozen(Player player)
boolean setFrozen(Player target, Player staff, boolean frozen)
```

**Available Events:**
- StaffVanishEvent, StaffModeToggleEvent, PlayerFreezeEvent
- StaffActionEvent, StaffChatEvent

---

### 3. VulcanToolsAPI

Manage currencies, boosters, and tool events.

> **Note:** These are **Bukkit Events** (`extends org.bukkit.event.Event`). Use Bukkit's event system to listen to them:
> ```java
> public class MyListener implements org.bukkit.event.Listener {
>     @org.bukkit.event.EventHandler
>     public void onToolEventStart(ToolEventStartEvent event) {
>         // Handle Bukkit event
>     }
> }
>
> // Register with Bukkit
> Bukkit.getPluginManager().registerEvents(new MyListener(), plugin);
> ```

**Basic Usage:**
```java
if (VulcanToolsAPI.isAvailable()) {
    VulcanToolsAPI api = VulcanToolsAPI.getInstance();
    ICurrencyManager currency = api.getCurrencyManager();

    currency.giveCurrency(player, "coins", 1000);
    long balance = currency.getBalance(player, "coins");
}
```

**Key Methods:**
```java
long getBalance(OfflinePlayer player, String currency)
void giveCurrency(OfflinePlayer player, String currency, long amount)
void removeCurrency(OfflinePlayer player, String currency, long amount)
boolean hasEnough(OfflinePlayer player, String currency, long amount)

double getTotalMultiplier(Player player, String boosterType, String target)
void applyBooster(Player player, String boosterType, String target, double multiplier, int duration)

void startEvent(ToolType eventType, int durationSeconds)
boolean isEventActive()
```

**Available Events:**
- ToolEventStartEvent, ToolEventEndEvent, ToolModeChangeEvent, ToolUpgradeEvent
- BoosterApplyEvent, CurrencyGrindEvent, CaneHarvestBatchAsyncEvent
- LumberHarvestEvent, FishCatchEvent, MobKillBatchAsyncEvent

---

### 4. VulcanEnchantsAPI

Manage custom enchantments, potion effects, and special abilities.

> **Note:** These are **Bukkit Events** (`extends org.bukkit.event.Event`). Use Bukkit's event system to listen to them:
> ```java
> public class MyListener implements org.bukkit.event.Listener {
>     @org.bukkit.event.EventHandler
>     public void onEnchantApply(CEApplyEvent event) {
>         // Handle Bukkit event
>     }
> }
>
> // Register with Bukkit
> Bukkit.getPluginManager().registerEvents(new MyListener(), plugin);
> ```

**Basic Usage:**
```java
if (VulcanEnchantsAPI.isAvailable()) {
    VulcanEnchantsAPI api = VulcanEnchantsAPI.getInstance();

    EnchantWrapper warrior = api.getEnchant("warrior");
    if (warrior != null) {
        int cost = warrior.getXpCost();
        String type = warrior.getEnchantType();
    }

    Set<String> enchants = api.getItemEnchants(item);
}
```

**Key Methods:**
```java
EnchantWrapper getEnchant(String key)
Set<String> getAllEnchantKeys()
boolean enchantExists(String key)
boolean isEnchantEnabled(String key)
ItemStack getEnchantBook(String key)

boolean isPotionEnchant(String key)
boolean applyPotionEnchant(Player player, String key, ItemStack item)
boolean removePotionEnchant(Player player, String key, ItemStack item)
int applyAllPotionEnchants(Player player)
int removeAllPotionEnchants(Player player)

boolean itemHasEnchant(ItemStack item, String key)
Set<String> getItemEnchants(ItemStack item)
boolean canApplyEnchant(ItemStack item, String key)
```

**Available Events:**
- CEApplyEvent - When enchant book is applied to item
- CEBuyEvent - When player purchases enchant book
- CEActivateEvent - When custom enchant effect triggers
- CEPotionApplyEvent - When potion effect is applied
- CEPotionRemoveEvent - When potion effect is removed

---

## Installation

This plugin is compiled and available through the Vulcan Loader found in the client panel [Here](https://vulcandev.net/).
## Dependencies

- VulcanEvents (optional)
- VulcanStaff (optional)
- VulcanTools (optional)
- VulcanEnchants (optional)
- Fortress (optional)

## Support

For support and questions, please contact the development team:
- **Authors**: Xanthard, OfficialGaming
- **Minecraft Version**: 1.7 - Latest

## License

This project is proprietary software developed by VulcanDev.
