# VulcanAPI

API for VulcanEvents, VulcanStaff, and VulcanTools.

> **⚠️ Critical**: Always wrap VulcanAPI initialization in try-catch blocks to prevent plugin failures when VulcanAPI is not present on the server.

## Current API Features

- **VulcanEventsAPI**: Manage server events, competitions, and player participation
- **VulcanStaffAPI**: Staff management tools including vanish, staff mode, and freeze functionality
- **VulcanToolsAPI**: Advanced tool system with currency management, boosters, and tool events

## API Usage

**⚠️ Important**: All VulcanAPI integrations require proper error handling to prevent plugin failures when the APIs are not available on the server. (e.g. When a server opts out of using the API)

### 1. VulcanEventsAPI

Manage server events and player participation.

#### Plugin Integration (Required)
```java
// In your plugin's onEnable() method
try {
    net.vulcandev.vulcanapi.vulcanevents.VulcanEventsAPI.initialize(vulcanEventsPlugin);
    getLogger().info("VulcanEventsAPI integration enabled successfully.");
} catch (NoClassDefFoundError e) {
    getLogger().warning("Failed to initialize VulcanEventsAPI: " + e.getMessage());
    getLogger().warning("Disabling Events API integration...");
    // Optionally disable API features in your config
}
```

##### Alternative Plugin Integration Method
You can also use the plugin manager to check for VulcanAPI availability and register event listeners conditionally:

```java
// Alternative method using plugin manager
Plugin plugin = getServer().getPluginManager().getPlugin("VulcanAPI");
if (plugin != null) {
    getServer().getPluginManager().registerEvents(new VulcanEventsListener(this), this);
}
```

#### Basic Usage
```java
import net.vulcandev.vulcanapi.vulcanevents.VulcanEventsAPI;

// Always check if the API is available before using it
if (VulcanEventsAPI.isAvailable()) {
    VulcanEventsAPI eventsAPI = VulcanEventsAPI.getInstance();

// Check if an event is currently active
if (eventsAPI.hasActiveEvent()) {
    String eventName = eventsAPI.getCurrentEventName();
    int participants = eventsAPI.getParticipantCount();
    System.out.println("Event '" + eventName + "' is active with " + participants + " participants");
}
```

#### Key Methods
```java
// Event Information
boolean hasActiveEvent()
IEvent getCurrentEvent()
EventType getCurrentEventType()
String getCurrentEventName()
EventState getCurrentEventState()
int getTimeRemaining()

// Player Management
boolean isPlayerInEvent(Player player)
boolean isPlayerSpectating(Player player)
boolean isPlayerBanned(Player player)
Map<UUID, EventPlayer> getParticipants()
Map<UUID, EventPlayer> getSpectators()

// Event Statistics
int getParticipantCount()
int getSpectatorCount()
boolean hasSpace()
```

#### Example: Event Monitor
```java
@EventHandler
public void onEventStart(EventStartEvent event) {
    String eventName = event.getEventName();
    EventType eventType = event.getEventType();
    
    Bukkit.broadcastMessage("§a" + eventName + " (" + eventType + ") has started!");
    
    // Schedule a task to announce participant count every 30 seconds
    Bukkit.getScheduler().runTaskTimer(plugin, () -> {
        VulcanEventsAPI api = VulcanEventsAPI.getInstance();
        if (api.hasActiveEvent()) {
            int count = api.getParticipantCount();
            Bukkit.broadcastMessage("§e" + count + " players participating!");
        }
    }, 600L, 600L); // 30 seconds in ticks
}
```

### 2. VulcanStaffAPI

Manage staff features including vanish, staff mode, and player freezing.

#### Plugin Integration (Required)
**Important**: Always wrap VulcanAPI initialization in a try-catch block to prevent your plugin from failing if VulcanAPI is not present on the server.

```java
// In your plugin's onEnable() method
try {
    net.vulcandev.vulcanapi.vulcanstaff.VulcanStaffAPI.initialize(this);
    getLogger().info("VulcanAPI integration enabled successfully.");
} catch (NoClassDefFoundError e) {
    getLogger().warning("Failed to initialize VulcanAPI: " + e.getMessage());
    getLogger().warning("Disabling API integration...");
    // Optionally disable API features in your config
    // getConfig().set("use-vulcan-api", false);
}
```

##### Alternative Plugin Integration Method
You can also use the plugin manager to check for VulcanAPI availability and register event listeners conditionally:

```java
// Alternative method using plugin manager
Plugin plugin = getServer().getPluginManager().getPlugin("VulcanAPI");
if (plugin != null) {
    getServer().getPluginManager().registerEvents(new VulcanStaffListener(this), this);
}
```

#### Basic Usage
```java
import net.vulcandev.vulcanapi.vulcanstaff.VulcanStaffAPI;

// Always check if the API is available before using it
if (VulcanStaffAPI.isAvailable()) {
    VulcanStaffAPI staffAPI = VulcanStaffAPI.getInstance();

    // Vanish a player
    Player player = Bukkit.getPlayer("PlayerName");
    boolean success = staffAPI.setVanished(player, true);

    if (success) {
        player.sendMessage("§aYou are now vanished!");
    }
} else {
    // Handle case where VulcanStaff is not available
    getLogger().warning("VulcanStaff is not available - API features disabled");
}
```

#### Key Methods
```java
// Vanish Management
boolean isVanished(Player player)
boolean isVanished(UUID uuid)
boolean setVanished(Player player, boolean vanished)
boolean canSeeVanished(Player player)
Set<UUID> getVanishedPlayers()

// Staff Mode
boolean isInStaffMode(Player player)
boolean isInStaffMode(UUID uuid)

// Freeze Management
boolean isFrozen(Player player)
boolean isFrozen(UUID uuid)
boolean setFrozen(Player target, Player staff, boolean frozen)
```

#### Example: Auto-Vanish for Staff
```java
@EventHandler
public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    // Auto-vanish staff members on join
    if (player.hasPermission("staff.autovanish")) {
        // Always check if VulcanStaff is available
        if (VulcanStaffAPI.isAvailable()) {
            VulcanStaffAPI staffAPI = VulcanStaffAPI.getInstance();
            if (staffAPI.setVanished(player, true)) {
                player.sendMessage("§7You have been automatically vanished.");
            }
        } else {
            // Fallback behavior when VulcanStaff is not available
            player.sendMessage("§cStaff features are currently unavailable.");
        }
    }
}

@EventHandler
public void onStaffVanish(StaffVanishEvent event) {
    Player player = event.getPlayer();
    boolean vanishing = event.isVanishing();

    if (vanishing) {
        player.sendMessage("§7You are now invisible to other players.");
    } else {
        player.sendMessage("§7You are now visible to other players.");
    }
}
```

### 3. VulcanToolsAPI

Manage currencies, boosters, and tool events.

#### Plugin Integration (Required)
```java
// In your plugin's onEnable() method
try {
    net.vulcandev.vulcanapi.vulcantools.VulcanToolsAPI.initialize(vulcanToolsPlugin);
    getLogger().info("VulcanToolsAPI integration enabled successfully.");
} catch (NoClassDefFoundError e) {
    getLogger().warning("Failed to initialize VulcanToolsAPI: " + e.getMessage());
    getLogger().warning("Disabling Tools API integration...");
    // Optionally disable API features in your config
}
```

##### Alternative Plugin Integration Method
You can also use the plugin manager to check for VulcanAPI availability and register event listeners conditionally:

```java
// Alternative method using plugin manager
Plugin plugin = getServer().getPluginManager().getPlugin("VulcanAPI");
if (plugin != null) {
    getServer().getPluginManager().registerEvents(new VulcanToolsListener(this), this);
}
```

#### Basic Usage
```java
import net.vulcandev.vulcanapi.vulcantools.VulcanToolsAPI;
import net.vulcandev.vulcanapi.vulcantools.interfaces.ICurrencyManager;

// Always check if the API is available before using it
if (VulcanToolsAPI.isAvailable()) {
    VulcanToolsAPI toolsAPI = VulcanToolsAPI.getInstance();
    ICurrencyManager currencyManager = toolsAPI.getCurrencyManager();

    // Give currency to a player
    Player player = Bukkit.getPlayer("PlayerName");
    currencyManager.giveCurrency(player, "coins", 1000);

    // Check player's balance
    long balance = currencyManager.getBalance(player, "coins");
    player.sendMessage("§aYour balance: " + balance + " coins");
} else {
    // Handle case where VulcanTools is not available
    getLogger().warning("VulcanTools is not available - currency features disabled");
}
```

#### Currency Management
```java
// Get currency manager
ICurrencyManager currencyManager = toolsAPI.getCurrencyManager();

// Currency operations
long getBalance(OfflinePlayer player, String currency)
void giveCurrency(OfflinePlayer player, String currency, long amount)
void removeCurrency(OfflinePlayer player, String currency, long amount)
boolean hasEnough(OfflinePlayer player, String currency, long amount)
boolean payCurrency(Player sender, OfflinePlayer receiver, String currency, long amount)

// Currency information
String getCurrencyIdByTool(String toolType)
Set<String> getAllCurrencyIds()
boolean currencyExists(String currency)
```

#### Booster Management
```java
// Get booster manager
IBoosterManager boosterManager = toolsAPI.getBoosterManager();

// Booster operations
double getTotalMultiplier(Player player, String boosterType, String target)
void applyBooster(Player player, String boosterType, String target, double multiplier, int durationSeconds)
boolean hasActiveBooster(Player player, String boosterType, String target)
void removeBooster(Player player, String boosterType, String target)
List<String> getActiveBoosterDescriptions(Player player)
```

#### Event Management
```java
// Get event manager
IEventManager eventManager = toolsAPI.getEventManager();

// Event operations
void startEvent(ToolType eventType, int durationSeconds)
void endEvent()
boolean isEventActive()
ToolType getEventType()
void addHarvest(UUID playerUUID, int amount, ToolType toolType)
int getHarvestAmount(UUID playerUUID)
LinkedHashMap<UUID, Integer> getSortedByHarvestedAmount()
List<String> getTopPlayers(int limit)
```

#### Example: Currency Shop
```java
public class CurrencyShop {
    private final VulcanToolsAPI toolsAPI;
    private final ICurrencyManager currencyManager;
    
    public CurrencyShop() {
        this.toolsAPI = VulcanToolsAPI.getInstance();
        this.currencyManager = toolsAPI.getCurrencyManager();
    }
    
    public boolean purchaseItem(Player player, String item, long cost) {
        if (!currencyManager.hasEnough(player, "coins", cost)) {
            player.sendMessage("§cInsufficient funds! You need " + cost + " coins.");
            return false;
        }
        
        currencyManager.removeCurrency(player, "coins", cost);
        
        // Give item to player (implementation depends on your item system)
        giveItemToPlayer(player, item);
        
        player.sendMessage("§aPurchased " + item + " for " + cost + " coins!");
        return true;
    }
    
    private void giveItemToPlayer(Player player, String item) {
        // Implementation for giving items
    }
}
```

## Events

VulcanAPI provides numerous events that you can listen to:

### VulcanEvents Events
- `EventStartEvent` - When an event starts
- `EventEndEvent` - When an event ends
- `EventStateChangeEvent` - When event state changes
- `PlayerJoinEventEvent` - When a player joins an event
- `PlayerLeaveEventEvent` - When a player leaves an event
- `PlayerEliminateEvent` - When a player is eliminated
- `PlayerSpectateEventEvent` - When a player starts spectating
- `KitPreApplyEvent` - Before a kit is applied to a player
- `KitPostApplyEvent` - After a kit is applied to a player

### VulcanStaff Events
- `StaffVanishEvent` - When staff vanish state changes
- `StaffModeToggleEvent` - When staff mode is toggled
- `PlayerFreezeEvent` - When a player is frozen/unfrozen
- `StaffActionEvent` - When staff performs logged actions
- `StaffChatEvent` - When staff sends messages in staff chat

### VulcanTools Events
- `ToolEventStartEvent` - When a tool event starts
- `ToolEventEndEvent` - When a tool event ends
- `ToolModeChangeEvent` - When tool mode changes
- `ToolUpgradeEvent` - When a tool is upgraded
- `BoosterApplyEvent` - When a booster is applied
- `CurrencyGrindEvent` - When currency is earned through grinding
- `CaneHarvestEvent` - When sugar cane is harvested
- `LumberHarvestEvent` - When wood is harvested
- `FishCatchEvent` - When fish is caught
- `MobKillEvent` - When a mob is killed with tools

### 1. Use Try-Catch for Initialization
Server may opt out of using the VulcanAPI, in which case it is best to not fully depend on it. If you do, your plugin will end up throwing an error if the API does not exist on the server
```java
@Override
public void onEnable() {
    // Initialize VulcanAPI integrations with proper error handling
    initializeVulcanAPIs();
}

private void initializeVulcanAPIs() {
    // VulcanStaff API
    try {
        net.vulcandev.vulcanapi.vulcanstaff.VulcanStaffAPI.initialize(this);
        getLogger().info("VulcanStaff API integration enabled.");
    } catch (NoClassDefFoundError e) {
        getLogger().warning("VulcanStaff API not found - staff features disabled.");
    }

    // VulcanTools API
    try {
        net.vulcandev.vulcanapi.vulcantools.VulcanToolsAPI.initialize(vulcanToolsPlugin);
        getLogger().info("VulcanTools API integration enabled.");
    } catch (NoClassDefFoundError e) {
        getLogger().warning("VulcanTools API not found - tool features disabled.");
    }

    // VulcanEvents API
    try {
        net.vulcandev.vulcanapi.vulcanevents.VulcanEventsAPI.initialize(vulcanEventsPlugin);
        getLogger().info("VulcanEvents API integration enabled.");
    } catch (NoClassDefFoundError e) {
        getLogger().warning("VulcanEvents API not found - event features disabled.");
    }
}
```

### 2. Handle Plugin Dependencies in plugin.yml
```yaml
name: YourPlugin
main: com.yourplugin.Main
version: 1.0.0
softdepend: [VulcanAPI, VulcanStaff, VulcanTools, VulcanEvents]
```

Using `softdepend` ensures your plugin loads after VulcanAPI if it's present, but won't fail if it's missing.

## Installation

1. Download the VulcanAPI jar file
2. Place it in your server's `plugins` folder
3. Restart your server
4. The API will automatically detect and integrate with VulcanEvents, VulcanStaff, and VulcanTools if they are present

## Dependencies

- Spigot/Paper 1.20+
- VulcanEvents (optional)
- VulcanStaff (optional)
- VulcanTools (optional)

## Support

For support and questions, please contact the development team:
- **Authors**: Xanthard, OfficialGaming
- **Version**: 1.0
- **Minecraft Version**: 1.20.1

## License

This project is proprietary software developed by VulcanDev.
