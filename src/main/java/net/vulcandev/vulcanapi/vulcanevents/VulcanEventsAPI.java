package net.vulcandev.vulcanapi.vulcanevents;

import net.vulcandev.vulcanapi.interfaces.events.IEventInstance;
import net.vulcandev.vulcanapi.interfaces.events.IPlayerBan;
import net.vulcandev.vulcanapi.interfaces.events.IVulcanEventsPlugin;
import net.vulcandev.vulcanapi.wrapper.EventStateWrapper;
import net.vulcandev.vulcanapi.wrapper.EventTypeWrapper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class VulcanEventsAPI {
    private static VulcanEventsAPI instance;
    private final IVulcanEventsPlugin plugin;

    public VulcanEventsAPI(IVulcanEventsPlugin plugin) {this.plugin = plugin;}

    /**
     * Gets the API instance
     * @return VulcanEventsAPI instance
     */
    public static VulcanEventsAPI getInstance() {return instance;}

    /**
     * Check if VulcanEvents is available and loaded
     * @return true if VulcanEvents is available, false otherwise
     */
    public static boolean isAvailable() {return instance != null && instance.plugin != null;}

    /**
     * Checks if there is currently an active event
     * @return true if an event is running, false otherwise
     */
    public boolean hasActiveEvent() {
        return plugin.getCurrentEvent() != null;
    }


    /**
     * Gets the type of the current event
     * @return EventTypeWrapper or null if no event is active
     */
    @Nullable
    public EventTypeWrapper getCurrentEventType() {
        if (!isAvailable()) return null;
        IEventInstance currentEvent = plugin.getCurrentEvent();
        if (currentEvent == null) return null;
        return EventTypeWrapper.fromString(currentEvent.getEventTypeName());
    }

    /**
     * Gets the name of the current event
     * @return event name or null if no event is active
     */
    @Nullable
    public String getCurrentEventName() {
        if (!isAvailable()) return null;
        IEventInstance currentEvent = plugin.getCurrentEvent();
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
        IEventInstance currentEvent = plugin.getCurrentEvent();
        if (currentEvent == null) return null;
        return EventStateWrapper.fromString(currentEvent.getStateName());
    }

    /**
     * Gets the time remaining in the current event
     * @return seconds remaining or -1 if no event is active
     */
    public int getTimeRemaining() {
        if (!isAvailable()) return -1;
        IEventInstance currentEvent = plugin.getCurrentEvent();
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
        IEventInstance currentEvent = plugin.getCurrentEvent();
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
        IEventInstance currentEvent = plugin.getCurrentEvent();
        if (currentEvent == null) return false;
        return currentEvent.getSpectatorMap().containsKey(player.getUniqueId());
    }

    /**
     * Gets the number of players currently participating in the event
     * @return number of participants or 0 if no event is active
     */
    public int getParticipantCount() {
        if (!isAvailable()) return 0;
        IEventInstance currentEvent = plugin.getCurrentEvent();
        if (currentEvent == null) return 0;
        return currentEvent.getPlayerMap().size();
    }

    /**
     * Gets the number of spectators currently watching the event
     * @return number of spectators or 0 if no event is active
     */
    public int getSpectatorCount() {
        if (!isAvailable()) return 0;
        IEventInstance currentEvent = plugin.getCurrentEvent();
        if (currentEvent == null) return 0;
        return currentEvent.getSpectatorMap().size();
    }

    /**
     * Gets all participants in the current event.
     *
     * @return a map of UUID to Player, or an empty map if no event is active
     */
    @NotNull
    public Map<UUID, Player> getParticipants() {
        if (!isAvailable()) return Collections.emptyMap();
        IEventInstance currentEvent = plugin.getCurrentEvent();
        if (currentEvent == null) return Collections.emptyMap();
        return currentEvent.getPlayerMap();
    }

    /**
     * Gets all spectators in the current event.
     *
     * @return a map of UUID to Player, or an empty map if no event is active
     */
    @NotNull
    public Map<UUID, Player> getSpectators() {
        if (!isAvailable()) return Collections.emptyMap();
        IEventInstance currentEvent = plugin.getCurrentEvent();
        if (currentEvent == null) return Collections.emptyMap();
        return currentEvent.getSpectatorMap();
    }


    /**
     * Checks if a player is banned from events
     * @param player The player to check
     * @return true if the player is banned, false otherwise
     */
    public boolean isPlayerBanned(@NotNull Player player) {
        if (!isAvailable()) return false;
        IPlayerBan ban = plugin.getPlayerBan(player.getUniqueId());
        return ban != null && ban.isStillBanned();
    }

    /**
     * Checks if there is space for more participants in the current event
     * @return true if there is space, false otherwise or if no event is active
     */
    public boolean hasSpace() {
        if (!isAvailable()) return false;
        IEventInstance currentEvent = plugin.getCurrentEvent();
        if (currentEvent == null) return false;
        return currentEvent.isSpace();
    }

    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin instanceof IVulcanEventsPlugin) {
            IVulcanEventsPlugin vulcanEvents = (IVulcanEventsPlugin) plugin;
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