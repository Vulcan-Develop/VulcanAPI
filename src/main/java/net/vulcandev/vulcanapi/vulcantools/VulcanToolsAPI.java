package net.vulcandev.vulcanapi.vulcantools;

import net.vulcandev.vulcanapi.vulcantools.interfaces.IBoosterManager;
import net.vulcandev.vulcanapi.vulcantools.interfaces.ICurrencyManager;
import net.vulcandev.vulcanapi.vulcantools.interfaces.IEventManager;
import net.vulcandev.vulcanapi.wrapper.ToolTypeWrapper;
import net.vulcandev.vulcantools.VulcanTools;
import net.vulcandev.vulcantools.enums.ToolType;
import net.vulcandev.vulcantools.managers.BoosterManager;
import net.vulcandev.vulcantools.managers.CurrencyManager;
import net.vulcandev.vulcantools.managers.EventManager;
import net.vulcandev.vulcantools.objects.Booster;
import net.vulcandev.vulcantools.objects.Currency;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Main API class for VulcanTools.
 * This class provides a clean interface for external plugins to interact with VulcanTools.
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * VulcanToolsAPI api = VulcanToolsAPI.getInstance();
 * if (api != null && VulcanToolsAPI.isAvailable()) {
 *     ICurrencyManager currencyManager = api.getCurrencyManager();
 *     // Use the currency manager...
 * }
 * }</pre>
 */
public class VulcanToolsAPI {
    private static VulcanToolsAPI instance;
    private final VulcanTools plugin;
    private final ICurrencyManager currencyManager;
    private final IEventManager eventManager;
    private final IBoosterManager boosterManager;

    public VulcanToolsAPI(VulcanTools plugin) {
        this.plugin = plugin;
        this.currencyManager = new CurrencyManagerWrapper(plugin.getCurrencyManager());
        this.eventManager = new EventManagerWrapper(plugin.getEventManager());
        this.boosterManager = new BoosterManagerWrapper(plugin.getBoosterManager());
    }

    /**
     * Gets the API instance.
     *
     * @return the VulcanToolsAPI instance, or null if VulcanTools is not loaded
     */
    public static VulcanToolsAPI getInstance() {return instance;}

    /**
     * Checks if VulcanTools is available and loaded.
     *
     * @return true if VulcanTools is available, false otherwise
     */
    public static boolean isAvailable() {return instance != null && instance.plugin != null && instance.plugin.isEnabled();}

    /**
     * Gets the currency manager interface.
     *
     * @return the currency manager interface
     */
    public ICurrencyManager getCurrencyManager() {
        return currencyManager;
    }

    /**
     * Gets the event manager interface.
     *
     * @return the event manager interface
     */
    public IEventManager getEventManager() {
        return eventManager;
    }

    /**
     * Gets the booster manager interface.
     *
     * @return the booster manager interface
     */
    public IBoosterManager getBoosterManager() {
        return boosterManager;
    }
    
    // Wrapper classes to implement the interfaces
    private static class CurrencyManagerWrapper implements ICurrencyManager {
        private final CurrencyManager manager;
        
        public CurrencyManagerWrapper(CurrencyManager manager) {
            this.manager = manager;
        }
        
        @Override
        public long getBalance(org.bukkit.OfflinePlayer player, String currency) {
            return manager.getBalance(player, currency);
        }
        
        @Override
        public void giveCurrency(org.bukkit.OfflinePlayer player, String currency, long amount) {
            manager.giveCurrency(player, currency, amount);
        }
        
        @Override
        public void removeCurrency(org.bukkit.OfflinePlayer player, String currency, long amount) {
            manager.removeCurrency(player, currency, amount);
        }
        
        @Override
        public boolean hasEnough(org.bukkit.OfflinePlayer player, String currency, long amount) {
            return manager.hasEnough(player, currency, amount);
        }
        
        @Override
        public boolean payCurrency(Player sender, org.bukkit.OfflinePlayer receiver, String currency, long amount) {
            return manager.payCurrency(sender, receiver, currency, amount);
        }
        
        @Override
        public String getCurrencyIdByTool(String toolType) {
            // Convert string to ToolType enum if needed
            try {
                net.vulcandev.vulcantools.enums.ToolType type = net.vulcandev.vulcantools.enums.ToolType.valueOf(toolType.toUpperCase());
                return manager.getCurrencyIdByTool(type);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        @Override
        public Set<String> getAllCurrencyIds() {
            return manager.getAllCurrencies().stream()
                .map(Currency::getKey)
                .collect(Collectors.toSet());
        }

        @Override
        public boolean currencyExists(String currency) {
            return manager.getAllCurrencies().stream()
                .anyMatch(c -> c.getKey().equalsIgnoreCase(currency));
        }
    }
    
    private static class EventManagerWrapper implements IEventManager {
        private final EventManager manager;
        
        public EventManagerWrapper(EventManager manager) {
            this.manager = manager;
        }
        
        @Override
        public void startEvent(ToolTypeWrapper eventType, int durationSeconds) {
            // Convert wrapper back to VulcanTools ToolType for internal use
            try {
                ToolType vulcanToolType = ToolType.valueOf(eventType.getType().name());
                manager.startEvent(vulcanToolType, durationSeconds);
            } catch (IllegalArgumentException e) {
                // Handle custom types - for now, we'll skip since VulcanTools won't recognize them
                throw new IllegalArgumentException("Unsupported tool type: " + eventType.getType());
            }
        }
        
        @Override
        public void endEvent() {
            manager.endEvent();
        }
        
        @Override
        public boolean isEventActive() {
            return manager.isEventActive();
        }
        
        @Override
        public ToolTypeWrapper getEventType() {
            ToolType vulcanType = manager.getEventType();
            return vulcanType != null ? ToolTypeWrapper.fromVulcanToolType(vulcanType) : null;
        }
        
        @Override
        public void addHarvest(UUID playerUUID, int amount, ToolTypeWrapper toolType) {
            try {
                ToolType vulcanToolType = ToolType.valueOf(toolType.getType().name());
                manager.addHarvest(playerUUID, amount, vulcanToolType);
            } catch (IllegalArgumentException e) {
                // Handle custom types - for now, we'll skip since VulcanTools won't recognize them
                throw new IllegalArgumentException("Unsupported tool type: " + toolType.getType());
            }
        }
        
        @Override
        public int getHarvestAmount(UUID playerUUID) {
            return manager.getHarvestAmount(playerUUID);
        }
        
        @Override
        public LinkedHashMap<UUID, Integer> getSortedByHarvestedAmount() {
            return manager.getSortedByHarvestedAmount();
        }

        @Override
        public List<String> getTopPlayers(int limit) {
            LinkedHashMap<UUID, Integer> sorted = manager.getSortedByHarvestedAmount();
            List<String> topPlayers = new ArrayList<>();

            int count = 0;
            for (Map.Entry<UUID, Integer> entry : sorted.entrySet()) {
                if (count >= limit) break;

                org.bukkit.OfflinePlayer player = org.bukkit.Bukkit.getOfflinePlayer(entry.getKey());
                String playerName = player.getName() != null ? player.getName() : "Unknown";
                topPlayers.add((count + 1) + ". " + playerName + " - " + entry.getValue());
                count++;
            }

            return topPlayers;
        }
    }
    
    private static class BoosterManagerWrapper implements IBoosterManager {
        private final BoosterManager manager;
        
        public BoosterManagerWrapper(BoosterManager manager) {
            this.manager = manager;
        }
        
        @Override
        public double getTotalMultiplier(Player player, String boosterType, String target) {
            return manager.getTotalMultiplier(player, boosterType, target);
        }
        
        @Override
        public void applyBooster(Player player, String boosterType, String target, double multiplier, int durationSeconds) {
            long expiryTime = System.currentTimeMillis() + (durationSeconds * 1000L);
            Booster booster = new Booster(boosterType, multiplier, expiryTime, player.getName(), null, target);
            manager.addPlayerBooster(player.getName(), booster);
        }

        @Override
        public boolean hasActiveBooster(Player player, String boosterType, String target) {
            return manager.getActiveBoosters(player).stream()
                .anyMatch(booster -> booster.appliesTo(boosterType, target) && !booster.isExpired());
        }

        @Override
        public void removeBooster(Player player, String boosterType, String target) {
            // Get all active boosters for the player
            List<Booster> activeBoosters = manager.getActiveBoosters(player);

            // Find and remove matching boosters (only player boosters, not team boosters)
            for (Booster booster : activeBoosters) {
                if (booster.appliesTo(boosterType, target) && booster.isPlayerBooster() &&
                    player.getName().equals(booster.getPlayerName())) {
                    // Remove the booster by ID
                    manager.removeBooster(booster.getId());
                    break; // Remove only the first matching booster
                }
            }
        }

        @Override
        public List<String> getActiveBoosterDescriptions(Player player) {
            return manager.getActiveBoosters(player).stream()
                .map(Booster::toString)
                .collect(Collectors.toList());
        }
    }

    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin.getClass().getName().equals("net.vulcandev.vulcantools.VulcanTools")) {
            VulcanTools vulcanTools = (VulcanTools) plugin;
            instance = new VulcanToolsAPI(vulcanTools);
        }
    }

    /**
     * Clean up the API instance
     */
    public static void cleanup() {
        instance = null;
    }
}
