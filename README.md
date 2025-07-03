# VulcanAPI

API for VulcanEvents, VulcanStaff, and VulcanTools.

## Features

- **VulcanEventsAPI**: Manage server events, competitions, and player participation
- **VulcanStaffAPI**: Staff management tools including vanish, staff mode, and freeze functionality
- **VulcanToolsAPI**: Advanced tool system with currency management, boosters, and tool events

## API Usage

### 1. VulcanEventsAPI

Manage server events and player participation.

#### Basic Usage
```java
import net.vulcandev.vulcanapi.vulcanevents.VulcanEventsAPI;

// Get the API instance
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

#### Basic Usage
```java
import net.vulcandev.vulcanapi.vulcanstaff.VulcanStaffAPI;

// Get the API instance
VulcanStaffAPI staffAPI = VulcanStaffAPI.getInstance();

// Check if VulcanStaff is available
if (VulcanStaffAPI.isAvailable()) {
    // Vanish a player
    Player player = Bukkit.getPlayer("PlayerName");
    boolean success = staffAPI.setVanished(player, true);
    
    if (success) {
        player.sendMessage("§aYou are now vanished!");
    }
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
        VulcanStaffAPI staffAPI = VulcanStaffAPI.getInstance();
        if (staffAPI != null) {
            staffAPI.setVanished(player, true);
            player.sendMessage("§7You have been automatically vanished.");
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

#### Basic Usage
```java
import net.vulcandev.vulcanapi.vulcantools.VulcanToolsAPI;
import net.vulcandev.vulcanapi.vulcantools.interfaces.ICurrencyManager;

// Get the API instance
VulcanToolsAPI toolsAPI = VulcanToolsAPI.getInstance();

if (VulcanToolsAPI.isAvailable()) {
    ICurrencyManager currencyManager = toolsAPI.getCurrencyManager();
    
    // Give currency to a player
    Player player = Bukkit.getPlayer("PlayerName");
    currencyManager.giveCurrency(player, "coins", 1000);
    
    // Check player's balance
    long balance = currencyManager.getBalance(player, "coins");
    player.sendMessage("§aYour balance: " + balance + " coins");
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
- `KitApplyEvent` - When a kit is applied to a player

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

## Support

For support and questions, please contact the development team:
- **Authors**: Xanthard, OfficialGaming
- **Version**: 1.0
- **Minecraft Version**: 1.20.1

## License

This project is proprietary software developed by VulcanDev.
