# VulcanAPI

API for VulcanEvents, VulcanStaff, VulcanTools, VulcanVoting and VulcanEnchants.

## Current API Features

- **VulcanEventsAPI**: Manage server events, competitions, and player participation
- **VulcanStaffAPI**: Staff management tools including vanish, staff mode, and freeze functionality
- **VulcanToolsAPI**: Advanced tool system with currency management, boosters, and tool events
- **VulcanEnchantsAPI**: Custom enchantments system with potion effects and special abilities

## Integration Guide

### Initialization (Recommended Pattern)

Use this method in your plugin's `onEnable()`:

> **⚠️ Important**: Do not add imports for the API classes at the top of your main plugin class (e.g., `import net.vulcandev.vulcanapi.vulcanevents.VulcanEventsAPI;`). If VulcanAPI is not present on the server, Java will fail to load your plugin class entirely, throwing a `NoClassDefFoundError` during plugin initialization. Instead, use fully qualified class names as shown below, which allows your plugin to load even when VulcanAPI is absent.

```java
private void initializeVulcanAPIs() {
    // Check if VulcanAPI plugin is present
    Plugin vulcanAPI = getServer().getPluginManager().getPlugin("VulcanAPI");
    if (vulcanAPI == null || !vulcanAPI.isEnabled()) {
        getLogger().warning("VulcanAPI not found - API features disabled");
        return;
    }

    // Now safe to use the APIs - use fully qualified names to avoid import issues
    if (net.vulcandev.vulcanapi.vulcanevents.VulcanEventsAPI.isAvailable()) {
        getLogger().info("VulcanEventsAPI enabled");
        // Initialize your VulcanEvents integration
    }

    if (net.vulcandev.vulcanapi.vulcanstaff.VulcanStaffAPI.isAvailable()) {
        getLogger().info("VulcanStaffAPI enabled");
        // Initialize your VulcanStaff integration
    }

    if (net.vulcandev.vulcanapi.vulcantools.VulcanToolsAPI.isAvailable()) {
        getLogger().info("VulcanToolsAPI enabled");
        // Initialize your VulcanTools integration
    }

    if (net.vulcandev.vulcanapi.vulcanenchants.VulcanEnchantsAPI.isAvailable()) {
        getLogger().info("VulcanEnchantsAPI enabled");
        // Initialize your VulcanEnchants integration
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
// Event Information
boolean hasActiveEvent()
String getCurrentEventName()
EventState getCurrentEventState()
int getTimeRemaining()

// Player Management
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

**Basic Usage:**
```java
if (VulcanStaffAPI.isAvailable()) {
    VulcanStaffAPI api = VulcanStaffAPI.getInstance();

    // Vanish a player
    api.setVanished(player, true);

    // Check if frozen
    boolean frozen = api.isFrozen(player);
}
```

**Key Methods:**
```java
// Vanish
boolean isVanished(Player player)
boolean setVanished(Player player, boolean vanished)
Set<UUID> getVanishedPlayers()

// Staff Mode
boolean isInStaffMode(Player player)

// Freeze
boolean isFrozen(Player player)
boolean setFrozen(Player target, Player staff, boolean frozen)
```

**Available Events:**
- StaffVanishEvent, StaffModeToggleEvent, PlayerFreezeEvent
- StaffActionEvent, StaffChatEvent

---

### 3. VulcanToolsAPI

Manage currencies, boosters, and tool events.

**Basic Usage:**
```java
if (VulcanToolsAPI.isAvailable()) {
    VulcanToolsAPI api = VulcanToolsAPI.getInstance();
    ICurrencyManager currency = api.getCurrencyManager();

    // Give currency
    currency.giveCurrency(player, "coins", 1000);

    // Check balance
    long balance = currency.getBalance(player, "coins");
}
```

**Key Methods:**
```java
// Currency Manager
long getBalance(OfflinePlayer player, String currency)
void giveCurrency(OfflinePlayer player, String currency, long amount)
void removeCurrency(OfflinePlayer player, String currency, long amount)
boolean hasEnough(OfflinePlayer player, String currency, long amount)

// Booster Manager
double getTotalMultiplier(Player player, String boosterType, String target)
void applyBooster(Player player, String boosterType, String target, double multiplier, int duration)

// Event Manager
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

**Basic Usage:**
```java
if (VulcanEnchantsAPI.isAvailable()) {
    VulcanEnchantsAPI api = VulcanEnchantsAPI.getInstance();

    // Get enchant info
    EnchantWrapper warrior = api.getEnchant("warrior");
    if (warrior != null) {
        int cost = warrior.getXpCost();
        String type = warrior.getEnchantType();
    }

    // Check item enchants
    Set<String> enchants = api.getItemEnchants(item);
}
```

**Key Methods:**
```java
// Enchant Information
EnchantWrapper getEnchant(String key)
Set<String> getAllEnchantKeys()
boolean enchantExists(String key)
boolean isEnchantEnabled(String key)
ItemStack getEnchantBook(String key)

// Potion Enchants
boolean isPotionEnchant(String key)
boolean applyPotionEnchant(Player player, String key, ItemStack item)
boolean removePotionEnchant(Player player, String key, ItemStack item)
int applyAllPotionEnchants(Player player)
int removeAllPotionEnchants(Player player)

// Item Checking
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

## Support

For support and questions, please contact the development team:
- **Authors**: Xanthard, OfficialGaming
- **Minecraft Version**: 1.7 - Latest

## License

This project is proprietary software developed by VulcanDev.
