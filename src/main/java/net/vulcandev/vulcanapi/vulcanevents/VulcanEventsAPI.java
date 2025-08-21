package net.vulcandev.vulcanapi.vulcanevents;

import net.vulcandev.vulcanapi.wrapper.EventStateWrapper;
import net.vulcandev.vulcanapi.wrapper.EventTypeWrapper;
import net.vulcandev.vulcanevents.VulcanEvents;
import net.vulcandev.vulcanevents.interfaces.IEvent;
import net.vulcandev.vulcanevents.objects.EventPlayer;
import net.vulcandev.vulcanevents.utils.EventPlayerBan;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
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
    public static boolean isAvailable() {return instance != null && instance.plugin != null && instance.plugin.isEnabled();}

    /**
     * Checks if there is currently an active event
     * @return true if an event is running, false otherwise
     */
    public boolean hasActiveEvent() {
        return plugin.getCurrentIEvent() != null;
    }


    /**
     * Gets the type of the current event
     * @return EventTypeWrapper or null if no event is active
     */
    @Nullable
    public EventTypeWrapper getCurrentEventType() {
        if (!isAvailable()) return null;
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) return null;
        return EventTypeWrapper.fromVulcanEventType(currentEvent.getEventType());
    }

    /**
     * Gets the name of the current event
     * @return event name or null if no event is active
     */
    @Nullable
    public String getCurrentEventName() {
        if (!isAvailable()) return null;
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) return null;
        return currentEvent.getName();
    }

    /**
     * Gets the current event state
     * @return EventStateWrapper or null if no event is active
     */
    @Nullable
    public EventStateWrapper getCurrentEventState() {
        if (!isAvailable()) return null;
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) return null;
        return EventStateWrapper.fromVulcanEventState(currentEvent.getState());
    }

    /**
     * Gets the time remaining in the current event
     * @return seconds remaining or -1 if no event is active
     */
    public int getTimeRemaining() {
        if (!isAvailable()) return -1;
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) return -1;
        return currentEvent.getSecondsLeft();
    }

    /**
     * Checks if a player is participating in the current event
     * @param player The player to check
     * @return true if the player is participating, false otherwise
     */
    public boolean isPlayerInEvent(@NotNull Player player) {
        if (!isAvailable()) return false;
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) return false;
        return currentEvent.isParticipating(player.getUniqueId());
    }

    /**
     * Checks if a player is spectating the current event
     * @param player The player to check
     * @return true if the player is spectating, false otherwise
     */
    public boolean isPlayerSpectating(@NotNull Player player) {
        if (!isAvailable()) return false;
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) return false;
        return currentEvent.getSpectators().containsKey(player.getUniqueId());
    }

    /**
     * Gets the number of players currently participating in the event
     * @return number of participants or 0 if no event is active
     */
    public int getParticipantCount() {
        if (!isAvailable()) return 0;
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) return 0;
        return currentEvent.getPlayers().size();
    }

    /**
     * Gets the number of spectators currently watching the event
     * @return number of spectators or 0 if no event is active
     */
    public int getSpectatorCount() {
        if (!isAvailable()) return 0;
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) return 0;
        return currentEvent.getSpectators().size();
    }

    /**
     * Gets all participants in the current event.
     *
     * @return a map of UUID to Player, or an empty map if no event is active
     */
    @NotNull
    public Map<UUID, Player> getParticipants() {
        if (!isAvailable()) return Collections.emptyMap();
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) return Collections.emptyMap();

        Map<UUID, Player> players = new HashMap<>();
        for (Map.Entry<UUID, EventPlayer> entry : currentEvent.getPlayers().entrySet()) {
            Player player = entry.getValue().getPlayer();
            if (player != null) {
                players.put(entry.getKey(), player);
            }
        }
        return players;
    }

    /**
     * Gets all spectators in the current event.
     *
     * @return a map of UUID to Player, or an empty map if no event is active
     */
    @NotNull
    public Map<UUID, Player> getSpectators() {
        if (!isAvailable()) return Collections.emptyMap();
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) return Collections.emptyMap();

        Map<UUID, Player> spectators = new HashMap<>();
        for (Map.Entry<UUID, EventPlayer> entry : currentEvent.getSpectators().entrySet()) {
            Player player = entry.getValue().getPlayer();
            if (player != null) {
                spectators.put(entry.getKey(), player);
            }
        }
        return spectators;
    }


    /**
     * Checks if a player is banned from events
     * @param player The player to check
     * @return true if the player is banned, false otherwise
     */
    public boolean isPlayerBanned(@NotNull Player player) {
        if (!isAvailable()) return false;
        EventPlayerBan ban = plugin.getBanned(player.getUniqueId());
        return ban != null && ban.isStillBanned();
    }

    /**
     * Checks if there is space for more participants in the current event
     * @return true if there is space, false otherwise or if no event is active
     */
    public boolean hasSpace() {
        if (!isAvailable()) return false;
        IEvent currentEvent = plugin.getCurrentIEvent();
        if (currentEvent == null) return false;
        return currentEvent.isSpace();
    }

    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin.getClass().getName().equals("net.vulcandev.vulcanevents.VulcanEvents")) {
            VulcanEvents vulcanEvents = (VulcanEvents) plugin;
            instance = new VulcanEventsAPI(vulcanEvents);
        }
    }

    /**
     * Clean up the API instance
     */
    public static void cleanup() {
        instance = null;
    }
}