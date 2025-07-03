package net.vulcandev.vulcanapi.vulcanevents;

import net.vulcandev.vulcanevents.VulcanEvents;
import net.vulcandev.vulcanevents.enums.EventState;
import net.vulcandev.vulcanevents.enums.EventType;
import net.vulcandev.vulcanevents.interfaces.IEvent;
import net.vulcandev.vulcanevents.objects.EventPlayer;
import net.vulcandev.vulcanevents.utils.EventPlayerBan;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VulcanEventsAPI {
    private static VulcanEventsAPI instance;
    private final VulcanEvents plugin;

    public VulcanEventsAPI(VulcanEvents plugin) {this.plugin = plugin;}

    /**
     * Gets the API instance
     * @return VulcanEventsAPI instance
     */
    public static VulcanEventsAPI getInstance() {return instance;}

    /**
     * Check if VulcanEvents is available and loaded
     * @return true if VulcanEvents is available, false otherwise
     */
    public static boolean isAvailable() {
        return instance != null && instance.plugin.isEnabled();
    }

    /**
     * Checks if there is currently an active event
     * @return true if an event is running, false otherwise
     */
    public boolean hasActiveEvent() {
        return plugin.getCurrentIEvent() != null;
    }

    /**
     * Gets the current active event
     * @return IEvent instance or null if no event is active
     */
    @Nullable
    public IEvent getCurrentEvent() {
        return plugin.getCurrentIEvent();
    }

    /**
     * Gets the type of the current event
     * @return EventType or null if no event is active
     */
    @Nullable
    public EventType getCurrentEventType() {
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) {
            return null;
        }
        return currentEvent.getEventType();
    }

    /**
     * Gets the name of the current event
     * @return event name or null if no event is active
     */
    @Nullable
    public String getCurrentEventName() {
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) {
            return null;
        }
        return currentEvent.getName();
    }

    /**
     * Gets the current event state
     * @return EventState or null if no event is active
     */
    @Nullable
    public EventState getCurrentEventState() {
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) {
            return null;
        }
        return currentEvent.getState();
    }

    /**
     * Gets the time remaining in the current event
     * @return seconds remaining or -1 if no event is active
     */
    public int getTimeRemaining() {
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) {
            return -1;
        }
        return currentEvent.getSecondsLeft();
    }

    /**
     * Checks if a player is participating in the current event
     * @param player The player to check
     * @return true if the player is participating, false otherwise
     */
    public boolean isPlayerInEvent(@NotNull Player player) {
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) {
            return false;
        }
        return currentEvent.isParticipating(player.getUniqueId());
    }

    /**
     * Checks if a player is spectating the current event
     * @param player The player to check
     * @return true if the player is spectating, false otherwise
     */
    public boolean isPlayerSpectating(@NotNull Player player) {
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) {
            return false;
        }
        return currentEvent.getSpectators().containsKey(player.getUniqueId());
    }

    /**
     * Gets the number of players currently participating in the event
     * @return number of participants or 0 if no event is active
     */
    public int getParticipantCount() {
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) {
            return 0;
        }
        return currentEvent.getPlayers().size();
    }

    /**
     * Gets the number of spectators currently watching the event
     * @return number of spectators or 0 if no event is active
     */
    public int getSpectatorCount() {
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) {
            return 0;
        }
        return currentEvent.getSpectators().size();
    }

    /**
     * Gets all participants in the current event
     * @return Map of UUID to EventPlayer or empty map if no event is active
     */
    @NotNull
    public Map<UUID, EventPlayer> getParticipants() {
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) {
            return new HashMap<>();
        }
        return new HashMap<>(currentEvent.getPlayers());
    }

    /**
     * Gets all spectators in the current event
     * @return Map of UUID to EventPlayer or empty map if no event is active
     */
    @NotNull
    public Map<UUID, EventPlayer> getSpectators() {
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) {
            return new HashMap<>();
        }
        return new HashMap<>(currentEvent.getSpectators());
    }

    /**
     * Checks if a player is banned from events
     * @param player The player to check
     * @return true if the player is banned, false otherwise
     */
    public boolean isPlayerBanned(@NotNull Player player) {
        EventPlayerBan ban = plugin.getBanned(player.getUniqueId());
        return ban != null && ban.isStillBanned();
    }

    /**
     * Checks if there is space for more participants in the current event
     * @return true if there is space, false otherwise or if no event is active
     */
    public boolean hasSpace() {
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) {
            return false;
        }
        return currentEvent.isSpace();
    }

    /**
     * Gets the VulcanEvents plugin instance
     * @return VulcanEvents plugin instance
     */
    @NotNull
    public VulcanEvents getPlugin() {
        return plugin;
    }

    public static void initialize(VulcanEvents plugin) {
        instance = new VulcanEventsAPI(plugin);
    }
}